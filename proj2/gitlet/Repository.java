package gitlet;

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;
import static java.lang.Math.min;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    // it is a branch point by path
    static public File HEAD = Utils.join(GITLET_DIR, "HEAD");


    /* TODO: fill in the rest of this class. */
    public static void setPersistent() throws IOException {
        // create the gitLet dir
        GITLET_DIR.mkdir();
        Branch.branches.mkdir();
        Commit.commitsFile.mkdir();
    }


    /**
     * init file
     *
     * @throws IOException
     */
    public static void initFile() throws IOException {
        setPersistent();
        Commit initCommit = new Commit();
        initCommit.savaCommit();
        FileStorage fileStorage = new FileStorage();
        Branch master = new Branch("master", initCommit.getSha1OfCommit());
        setHEAD("master");
    }

    /**
     * set HEAD by path to branch or commit file
     *
     * @param branchName
     * @throws IOException
     */
    public static void setHEAD(String branchName) throws IOException {
        if (!HEAD.exists()) {
            HEAD.createNewFile();
        }
        // check the branch if exist in outside
        File fileBranch = Utils.join(Branch.branches, branchName);
        String path = fileBranch.getAbsolutePath();
        Utils.writeContents(HEAD, path);
    }

    /**
     * add file to the StagingArea
     */
    public static void add(String filename) throws IOException {
        // TODO  if add file have same content with staging area
        // if rm area have the add file delete it from rm area
        // get rm area what name
        if (StagingArea.rmStagingArea.exists()) {
            LinkedList<String> linkedListToRmFilenames = StagingArea.loadRmAreaToList();
            for (String name : linkedListToRmFilenames) {
                if (filename.equals(name)) {
                    StagingArea.clearRmArea();
                    System.exit(0);
                }
            }
        }

        StagingArea stagingArea = new StagingArea(true);
        stagingArea.gitLetAdd(filename);
    }

    /**
     * update branch by branch file
     */
    private static void upDateBranchByFile(File branchFile, String commitSha1) {
        Utils.writeContents(branchFile, commitSha1);
    }


    /**
     * commit from staging area
     */
    public static void commit(String message) throws IOException {
        // if staging area and rm area does not exist
        if (!StagingArea.stagingArea.exists() && !StagingArea.rmStagingArea.exists()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // use HEAD to find last branch
        String HEAD = Commit.findLatestCommitSha1();

        // create a new commit and the parent is last commit
        Commit commit = new Commit(message, HEAD);

        File mainBranch = Branch.findMainBranch();
        // update the master or other branch of this commit
        upDateBranchByFile(mainBranch, commit.getSha1OfCommit());


        //set the HEAD point to this branch
        setHEAD(mainBranch.getName());

        // save the new commit
        commit.savaCommit();
    }

    /**
     * get untracked file for current commit
     */
    public static List<String> getUntrackedFileOfCurrentCommit() {
        List<String> listOfUntrackedFiles = getUntrackedFileByCommitSha1(Commit.findLatestCommitSha1());
        if (StagingArea.stagingArea.exists()) {
            LinkedList<Blob> blobLinkedList = StagingArea.getBlobListForThisStagingArea();
            for (Blob b : blobLinkedList) {
                if (listOfUntrackedFiles.contains(b.getFilename())) {
                    listOfUntrackedFiles.remove(b.filename);
                }
            }
        }
        return listOfUntrackedFiles;
    }


    /**
     * rm a file from gitLet
     */
    public static void rm(String filenameToRm) throws IOException {
        List<String> unTrackedFile = getUntrackedFileOfCurrentCommit();
        // if the file is untracked
        if (unTrackedFile.contains(filenameToRm)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        // create a staging area
        StagingArea stagingArea = new StagingArea(false);
        File file = Utils.join(CWD, filenameToRm);
        // check the file if in the working direction
        if (file.exists()) {
            String fileSha1 = Blob.getSha1ByFile(file);
            // unstage te file if it is currently staged
            if (StagingArea.stagingArea.exists()) {
                if (StagingArea.ifStagingAreaHaveTheFile(fileSha1)) {
                    StagingArea.deleteBlobFromStagingArea(fileSha1);
                }
            }
        }


        //  if the file is tracked in the current commit
        TreeMap<String, String> currentCommitBlobPoints = Commit.getCurrentCommitBlobPoints();
        for (String x : currentCommitBlobPoints.keySet()) {
            if (x.equals(filenameToRm)) {
                // put the rm file name into rm area and delete this name in staging area
                stagingArea.rmNameIntoRmArea(filenameToRm);
                // delete the file in working direction
                Utils.restrictedDelete(file);
            }
        }
    }


    /**
     * log
     */
    public static void log() throws IOException {
        // find latest commit of head
        String HEAD = Commit.findLatestCommitSha1();


        // find the commit by sha1
        Commit commit = Commit.findCommitByCommitSha1(HEAD);

        while (true) {
            // find the commit id
            String commitId = commit.getSha1OfCommit();
            // get the format date
            String formatDate = commit.getForMatDate();
            // get the message
            String message = commit.getMessage();
            //print out the head and iterator
            System.out.println("===");
            System.out.println("commit " + commitId);
            if (commit.getSecondParent() != null) {
                System.out.println("Merge: " + commit.getMergeMessage());
            }
            System.out.println("Date: " + formatDate);
            System.out.println(message);
            // if the commit parent is null then break
            if (commit.getParent() == null) {
                break;
            }
            System.out.println();
            // update the commit to its parent
            commit = Commit.findCommitByCommitSha1(commit.getParent());
        }


    }


    /**
     * Like log, except displays information about all commits ever made.
     * The order of the commits does not matter.
     * Hint: there is a useful method in gitlet.Utils that
     * will help you iterate over files within a directory.
     */
    public static void globalLog() {
        List<String> list = Utils.plainFilenamesIn(Commit.commitsFile);
        for (String s : list) {
            Commit commit = Commit.findCommitByCommitSha1(s);
            // find the commit id
            String commitId = s;
            // get the format date
            String formatDate = commit.getForMatDate();
            // get the message
            String message = commit.getMessage();
            //print out the head and iterator
            System.out.println("===");
            System.out.println("commit " + commitId);
            System.out.println("Date: " + formatDate);
            System.out.println(message);
            System.out.println();
        }
    }

    /**
     * Prints out the ids of all commits that
     * have the given commit message, one per line.
     * If there are multiple such commits,
     * it prints the ids out on separate lines.
     * The commit message is a single operand;
     * to indicate a multiword message, put the operand in quotation marks, as for the commit command below. Hint: the hint for this command is the same as the one for global-log.
     */
    public static void find(String commitMessage) {
        List<String> list = Utils.plainFilenamesIn(Commit.commitsFile);
        List<String> list_message = new ArrayList<>();

        for (String s : list) {
            Commit commit = Commit.findCommitByCommitSha1(s);
            // find the commit id
            String commitId = s;
            // get the message
            String message = commit.getMessage();
            // add the message to list_message
            list_message.add(message);
            // if the message is equal what I want to find
            if (message.equals(commitMessage)) {
                System.out.println(commitId);
            }
        }

        // failure cases: if no such commit exists, prints the error message
        if (!list_message.contains(commitMessage)) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * Displays what branches currently exist,
     * and marks the current branch with a *.
     * Also displays what files have been staged
     * for addition or removal.
     * An example of the exact format
     * it should follow is as follows.
     */
    public static void status() {
        // get the head point to which commit
        List<String> list = Utils.plainFilenamesIn(Branch.branches);

        // the branch Headline
        System.out.println("=== Branches ===");
        File file = Branch.findMainBranch();
        System.out.println("*" + file.getName());

        // find the * branch first
        for (String x : list) {
            if (x.equals(file.getName())) {
                continue;
            }
            System.out.println(x);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        List<String> stagingAreaFilename = new ArrayList<>();
        if (StagingArea.stagingArea.exists()) {
            LinkedList<Blob> linkedListBlob = Utils.readObject(StagingArea.stagingArea, LinkedList.class);
            for (Blob b : linkedListBlob) {
                stagingAreaFilename.add(b.getFilename());
            }
            Collections.sort(stagingAreaFilename);// lexicographic order out filename

            for (String filename : stagingAreaFilename) {
                System.out.println(filename);
            }
        }
        System.out.println();


        System.out.println("=== Removed Files ===");
        if (StagingArea.rmStagingArea.exists()) {
            LinkedList<String> rmFileList = StagingArea.loadRmAreaToList();
            for (String filename : rmFileList) {
               System.out.println(filename);
            }
        }
        System.out.println();


        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();


        System.out.println("=== Untracked Files ===");
        List<String> untrackedFileName = getUntrackedFileOfCurrentCommit();
        Collections.sort(untrackedFileName);
        for (String filename : untrackedFileName) {
            System.out.println(filename);
        }
        System.out.println();
    }

    /**
     * deletes the branch with the given name
     */
    public static void deleteBranch(String branchName) {
        File fileBranch = Utils.join(Branch.branches, branchName);
        String pathToHEAD = Utils.readContentsAsString(HEAD);

        // if a branch with the given name does not exist
        if (!fileBranch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        // if you try to remove the branch you are currently on
        if (pathToHEAD.equals(fileBranch.getAbsolutePath())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        // delete the branch form branches directory
        fileBranch.delete();
    }

    /**
     * get the nearest key
     * @throws IOException
     */
    private static String getNearKey(TreeSet<String> treeSet, String target) {
        String closerHigher = treeSet.ceiling(target);

        return closerHigher;
    }



    /**
     * check the file and change the file in the
     * working directory by different commit
     */
    public static void checkout(String commitId, String filename) throws IOException {
        if (commitId.length() < 30) {
            List<String> listOfCommitFile = Utils.plainFilenamesIn(Commit.commitsFile);
            TreeSet<String> treeMapOfCommitID = new TreeSet();
            for (String fileNameofCommitFile : listOfCommitFile) {
                treeMapOfCommitID.add(fileNameofCommitFile);
            }
            commitId = getNearKey(treeMapOfCommitID, commitId);
        }
        // find the commit
        File commitFile = Utils.join(Commit.commitsFile, commitId);
        if (!commitFile.exists()) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }
        Commit commit = Commit.findCommitByCommitSha1(commitId);


        TreeMap<String, String> BlobPoints = commit.getBlobPoints();
        if (BlobPoints.keySet().contains(filename)) {
            String fileSha1 = BlobPoints.get(filename);
            FileStorage fileStorage = FileStorage.loadFileStorage();
            // get the file content of this file
            byte[] fileContent = fileStorage.getContentBySHA1(fileSha1);
            // change the file in the working directory
            changeFileContentInWorkingDirectory(filename, fileContent);
        } else {
            Utils.message("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /**
     * create a new branch with the given name, and point
     * it at the current head commit
     */
    public static void createBranch(String branchName) throws IOException {
        File branchFile = Utils.join(Branch.branches, branchName);
        if (branchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        String latestCommitSha1 = Commit.findLatestCommitSha1();
        Branch branch = new Branch(branchName, latestCommitSha1);
    }

    /**
     * get untracked file from working directory from different branch
     */
    private static List<String> getUntrackedFileByBranchName(String branchName) {
        String commitId = Branch.checkupCommitIdByBranchName(branchName);
        return getUntrackedFileByCommitSha1(commitId);
    }

    /**
     * get untracked file by commit id
     */
    private static List<String> getUntrackedFileByCommitSha1(String commitId) {
        Commit thisCommit = Commit.findCommitByCommitSha1(commitId);
        TreeMap<String, String> fileToFileSha1 = thisCommit.getBlobPoints();
        List<String> listOfWorkingDirectoryFile = Utils.plainFilenamesIn(CWD);
        List<String> untrackedFileName = new ArrayList<>();
        for (String filename : listOfWorkingDirectoryFile) {
            if (fileToFileSha1.keySet().contains(filename)) {
                continue;
            }
            untrackedFileName.add(filename);
        }
        return untrackedFileName;
    }

    /**
     * get HEAD string path
     */
    private static String getHEADPath() {
        return Utils.readContentsAsString(HEAD);

    }

    /**
     * set HEAD to commit
     */
    private static void setHEADToCommit(String commitId) {
        File commitFile = Utils.join(Commit.commitsFile, commitId);
        Utils.writeContents(HEAD, commitFile.getAbsolutePath());
    }

    /**
     * checkup the branch
     */
    public static void checkout(String branchName) throws IOException {
        File file = Utils.join(Branch.branches, branchName);
        // check the branch if exist
        if (!file.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        String thisBranchCommitId = Branch.checkupCommitIdByBranchName(branchName);

        // if that branch is the current branch
        if (getHEADPath().equals(file.getAbsolutePath())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        List<String> unTrackedFilename = getUntrackedFileOfCurrentCommit();
        if (!unTrackedFilename.isEmpty()) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
        }


        // get the commit by commit id
        Commit thisBranchCommit = Commit.findCommitByCommitSha1(thisBranchCommitId);
        TreeMap<String, String> thisCommitBlobPoints = thisBranchCommit.getBlobPoints();
        for (String filename : thisCommitBlobPoints.keySet()) {
            String fileSha1 = thisCommitBlobPoints.get(filename);
            FileStorage fileStorage = FileStorage.loadFileStorage();
            byte[] fileContent = fileStorage.getContentBySHA1(fileSha1);
            changeFileContentInWorkingDirectory(filename, fileContent);
        }

        // delete all the untracked file
        List<String> untrackedFile = getUntrackedFileByBranchName(branchName);
        for (String untrackedFilename : untrackedFile) {
            File untrackedFileToDelete = Utils.join(CWD, untrackedFilename);
            Utils.restrictedDelete(untrackedFileToDelete);
        }

        // clear the staging area
        StagingArea.clearStagingArea();

        //set main branch to this branch
        setHEAD(branchName);
    }

    /**
     * checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit node
     */
    public static void reset(String commitId) throws IOException {
        File file = Utils.join(Commit.commitsFile, commitId);
        // check the branch if exist
        if (!file.exists()) {
            Utils.message("No commit with that id exists.", file);
            System.exit(0);
        }

        List<String> unTrackedFile = getUntrackedFileOfCurrentCommit();
        if (!unTrackedFile.isEmpty()) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }


        // delete all the untracked file
        List<String> untrackedFileToGivenCommit = getUntrackedFileByCommitSha1(commitId);
        for (String untrackedFilename : untrackedFileToGivenCommit) {
            File untrackedFileToDelete = Utils.join(CWD, untrackedFilename);
            Utils.restrictedDelete(untrackedFileToDelete);
        }

        // move the head to this commit
        File mainBranchFile = Branch.findMainBranch();
        Utils.writeContents(mainBranchFile, commitId);

        // clear the staging area
        StagingArea.clearStagingArea();
        StagingArea.clearRmArea();

        // get the commit by commit id and change file content in the working directory
        Commit thisBranchCommit = Commit.findCommitByCommitSha1(commitId);
        TreeMap<String, String> thisCommitBlobPoints = thisBranchCommit.getBlobPoints();
        for (String filename : thisCommitBlobPoints.keySet()) {
            String fileSha1 = thisCommitBlobPoints.get(filename);
            FileStorage fileStorage = FileStorage.loadFileStorage();
            byte[] fileContent = fileStorage.getContentBySHA1(fileSha1);
            changeFileContentInWorkingDirectory(filename, fileContent);
        }
    }

    /**
     * change file content in working directory
     */
    private static void changeFileContentInWorkingDirectory(String filename, byte[] fileContent) throws IOException {
        File fileOfCommitToWorkingDirectory = Utils.join(CWD, filename);
        if (!fileOfCommitToWorkingDirectory.exists()) {
            fileOfCommitToWorkingDirectory.createNewFile();
        }
        Utils.writeContents(fileOfCommitToWorkingDirectory, fileContent);
    }

    /**
     * final boss merge two branches
     */
    public static void merge(String BranchName) throws IOException {
        // if there are staged additions or removals present
        if (StagingArea.stagingArea.exists() || StagingArea.rmStagingArea.exists()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }

        File file10 = Utils.join(Branch.branches, BranchName);
        if (!file10.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }


        // get the file storage
        FileStorage fileStorage = FileStorage.loadFileStorage();
        // get the current commit
        Commit currentCommit = Commit.findLatestCommit();
        File file = Branch.findMainBranch();
        String currentBranchName = file.getName();
        // get the given branch
        Commit givenCommit = Branch.getCommitByBranchName(BranchName);
        // get the split point of the given branch and current branch
        Commit splitPoint = currentCommit.findTheSplitPoint(BranchName);
        if (givenCommit.equals(currentCommit)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        List<String> currentBranch = currentCommit.getWholeBranchOfThisCommit();
        if (currentBranch.contains(givenCommit.getSha1OfCommit()) && !givenCommit.equals(currentCommit) ) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }


        //  if the split point is the current branch
        if (currentCommit.equals(splitPoint)) {
            //  check out the given branch, and the operation ends after printing
            System.out.println("Current branch fast-forwarded.");
            checkout(BranchName);
            System.exit(0);
        }

        List<String> unTrackedFile = getUntrackedFileOfCurrentCommit();
        if (!unTrackedFile.isEmpty()) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }


        TreeMap<String, String> splitPointBlobPoints = splitPoint.getBlobPoints();
        // TODO get the file modified in current branch
        TreeMap<String, String> currentBranchBlobPoints = currentCommit.getBlobPoints();
        TreeMap<String, String> givenBranchBlobPoints = givenCommit.getBlobPoints();

        // first four conditions
        for (String filename : splitPointBlobPoints.keySet()) {
            String splitPointFileSha1 = splitPointBlobPoints.get(filename);
            if (givenBranchBlobPoints.containsKey(filename) && currentBranchBlobPoints.containsKey(filename)) {
                String givenBranchSha1 = givenBranchBlobPoints.get(filename);
                String currentBranchSha1 = currentBranchBlobPoints.get(filename);
                // first four condition
                if (!givenBranchSha1.equals(splitPointFileSha1) && currentBranchSha1.equals(splitPointFileSha1)) {
                    StagingArea.addFileByFileId(givenBranchSha1);
                    // create new content in working directory
                    File file1 = Utils.join(CWD, filename);
                    byte[] content = fileStorage.getContentBySHA1(givenBranchSha1);
                    if (!file1.exists()) {
                        file1.createNewFile();
                    }
                    Utils.writeContents(file1, content);

                } else if (!givenBranchSha1.equals(splitPointFileSha1) && !currentBranchSha1.equals(splitPointFileSha1) && !currentBranchSha1.equals(givenBranchSha1)) { // merge condition
                    // conflict
                    mergeConflict(currentBranchSha1, givenBranchSha1);
                }

            } else if (!givenBranchBlobPoints.containsKey(filename) && currentBranchBlobPoints.containsKey(filename)) {
                String currentFileSha1 = currentBranchBlobPoints.get(filename);
                if (currentFileSha1.equals(splitPointFileSha1)) {
                    rm(filename);
                } else {
                    // it is a merge conflict
                    mergeConflict(currentFileSha1, null);
                }
            } else if(!currentBranchBlobPoints.containsKey(filename) && givenBranchBlobPoints.containsKey(filename)) {
                String givenFileSha1 = givenBranchBlobPoints.get(filename);
                if (!givenFileSha1.equals(splitPointFileSha1)) {
                    mergeConflict(null, givenFileSha1);
                }
            }
        }

        // last two condition
        for (String filename : givenBranchBlobPoints.keySet()) {
            String givenFileSha1 = givenBranchBlobPoints.get(filename);
            if (!splitPointBlobPoints.containsKey(filename)) {
                if (!currentBranchBlobPoints.containsKey(filename)) {

                    StagingArea.addFileByFileId(givenFileSha1);
                    File file1 = Utils.join(CWD, filename);
                    // create new content in working directory
                    byte[] content = fileStorage.getContentBySHA1(givenFileSha1);
                    if (!file1.exists()) {
                        file1.createNewFile();
                    }
                    Utils.writeContents(file1, content);
                } else {
                    String currentFileSha1 = currentBranchBlobPoints.get(filename);
                    if (!currentFileSha1.equals(givenFileSha1)) {
                        mergeConflict(currentFileSha1, givenFileSha1);
                    }
                }
            }

        }

        // commit this two branch
        String commitMessage = "Merged " + BranchName + " " + "into " + currentBranchName + "." ;
        commit(commitMessage, givenCommit.getSha1OfCommit());
        // TODO any files have been modified in the given branch since the split point, but not modified in the current branch since the split point should be changed to their versions in the given branch
        // TODO checked out from the com
        /**
         * TODO Any files that have been modified in the current branch
         * but not in the given branch since the split point
         * should stay as they are.
         */
    }

    /**
     * conflict condition of merge
     */
    private static File mergeConflict(String currentSha1, String givenBranchSha1) {
        System.out.println("Encountered a merge conflict.");
        FileStorage fileStorage = FileStorage.loadFileStorage();

        String name;
        if (currentSha1.equals(null)) {
            name = fileStorage.getNameByFileId(givenBranchSha1);
        }

        name = fileStorage.getNameByFileId(currentSha1);

        // get the both content
        byte[] currentContent = fileStorage.getContentBySHA1(currentSha1);
        byte[] givenContent = fileStorage.getContentBySHA1(givenBranchSha1);
        if (currentContent == null) {
            currentContent = new byte[]{
                    13, 10
            };
        } else if (givenContent == null) {
            givenContent = new byte[]{
                    13, 10
            };
        }
        File file = Utils.join(CWD, name);
        Utils.writeContents(file, "<<<<<<< HEAD\n", currentContent,   "=======\n", givenContent,  ">>>>>>>" + "\n");
        return file;
    }

    /**
     *  If an untracked file in the current commit would be overwritten
     *  or deleted by the merge, print There is an untracked
     *  file in the way; delete it, or add and commit it
     *  first. and exit
     */
    private static void mergeHelperToCheckUntrackedFileBeChanged(String filename) {
        List<String> unTrackedFile = getUntrackedFileOfCurrentCommit();
        // if an untracked file in the current commit would be overwritten or deleted by the merge
        if (unTrackedFile.contains(filename)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }
    }

    /**
     * two parent merge commit
     * @param message
     * @throws IOException
     */

    public static void commit(String message, String givenBranch) throws IOException {
        // if staging area and rm area does not exist
        if (!StagingArea.stagingArea.exists() && !StagingArea.rmStagingArea.exists()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // use HEAD to find last branch
        String HEAD = Commit.findLatestCommitSha1();

        // create a new commit and the parent is last commit
        Commit commit = new Commit(message, HEAD, givenBranch);

        File mainBranch = Branch.findMainBranch();
        // update the master or other branch of this commit
        upDateBranchByFile(mainBranch, commit.getSha1OfCommit());


        //set the HEAD point to this branch
        setHEAD(mainBranch.getName());

        // save the new commit
        commit.savaCommit();
    }
}
