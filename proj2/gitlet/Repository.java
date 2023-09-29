package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;

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

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
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
     * set HEAD by path to branch
     * @param branchName
     * @throws IOException
     */
    public static void setHEAD(String branchName) throws IOException {
        // check the branch if exist in outside
        File fileBranch = Utils.join(Branch.branches, branchName);
        String path = fileBranch.getAbsolutePath();
        if (!HEAD.exists()) {
            HEAD.createNewFile();
        }
        Utils.writeContents(HEAD, path);
    }

    /**
     * add file to the StagingArea
     */
    public static void add(String filename) throws IOException {
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
        // use HEAD to find last branch
        String HEAD =  Commit.findLatestCommitSha1();

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
     * rm a file from gitLet
     */
    public static void rm(String filenameToRm) throws IOException {
        // create a staging area
        StagingArea stagingArea = new StagingArea(false);

        // put the rm file name into rm area and delete this name in staging area
        stagingArea.rmNameIntoRmArea(filenameToRm);

        // check the file if in the working direction
        File file = Utils.join(CWD, filenameToRm);
        if (file.exists()) {
            // delete the file in working direction
            Utils.restrictedDelete(file);
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
     *  Like log, except displays information about all commits ever made.
     *  The order of the commits does not matter.
     *  Hint: there is a useful method in gitlet.Utils that
     *  will help you iterate over files within a directory.
     */
    public static void globalLog() {
        List<String> list =  Utils.plainFilenamesIn(Commit.commitsFile);
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
        List<String> list =  Utils.plainFilenamesIn(Commit.commitsFile);
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
                System.out.println("commit " + commitId);
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
        // check the branches file
        String commitSha1Head = Commit.findLatestCommitSha1();
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
        if (StagingArea.stagingArea.exists()) {
            LinkedList<Blob> linkedListBlob = Utils.readObject(StagingArea.stagingArea, LinkedList.class);
            List<String> stagingAreaFilename = new ArrayList<>();
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
            String rmFilename = Utils.readContentsAsString(StagingArea.rmStagingArea);
            System.out.println(rmFilename);
        }
        System.out.println();


        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();


        System.out.println("=== Untracked Files ===");
        List<String> untrackedFileName = getUntrackedFileByBranchName(Branch.findMainBranch().getName());
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
            throw Utils.error("A branch with that name does not exist.");
        }

        // if you try to remove the branch you are currently on
        if (pathToHEAD.equals(fileBranch.getAbsolutePath())) {
            throw Utils.error("Cannot remove the current branch.");
        }

        // delete the branch form branches directory
        Utils.restrictedDelete(fileBranch);
    }




    /**
     * check the file and change the file in the
     * working directory by different commit
     */
    public static void checkout(String commitId, String filename) throws IOException {
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
            byte[] fileContent = fileStorage.getContentBySHA1(fileSha1, filename);
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
        Branch branch = new Branch(branchName, Commit.findLatestCommitSha1());
        setHEAD(branchName);
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
        List<String> untrackedFileName  = new ArrayList<>();
        for (String filename : listOfWorkingDirectoryFile) {
            if (fileToFileSha1.keySet().contains(filename)) {
                continue;
            }
            untrackedFileName.add(filename);
        }
        return untrackedFileName;
    }


    /**
     * checkup the branch
     */
    public static void checkout(String branchName) throws IOException {
        File file = Utils.join(Branch.branches, branchName);
        // check the branch if exist
        if (!file.exists()) {
            Utils.message("No such branch exists", file);
            System.exit(0);
        }

        // check the branch first
        String HeadCommitId = Commit.findLatestCommitSha1();
        String thisBranchCommitId = Branch.checkupCommitIdByBranchName(branchName);

        // if that branch is the current branch
        if (HeadCommitId.equals(thisBranchCommitId)) {
            Utils.message("No need to checkout the current branch.");
            System.exit(0);
        }

        // get the commit by commit id
        Commit thisBranchCommit = Commit.findCommitByCommitSha1(thisBranchCommitId);
        TreeMap<String, String> thisCommitBlobPoints = thisBranchCommit.getBlobPoints();
        for (String filename : thisCommitBlobPoints.keySet()) {
            String fileSha1 = thisCommitBlobPoints.get(filename);
            FileStorage fileStorage = FileStorage.loadFileStorage();
            byte[] fileContent = fileStorage.getContentBySHA1(fileSha1, filename);
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
        String HeadCommitId = Commit.findLatestCommitSha1();
        List<String> unTrackedFileOfCurrentBranch = getUntrackedFileByCommitSha1(HeadCommitId);

        String thisBranchCommitId = commitId;

        // get the commit by commit id
        Commit thisBranchCommit = Commit.findCommitByCommitSha1(thisBranchCommitId);
        TreeMap<String, String> thisCommitBlobPoints = thisBranchCommit.getBlobPoints();
        for (String filename : thisCommitBlobPoints.keySet()) {
            String fileSha1 = thisCommitBlobPoints.get(filename);
            FileStorage fileStorage = FileStorage.loadFileStorage();

            // if a working file is untracked in the current branch and would be overwritten by the reset
            if (unTrackedFileOfCurrentBranch.contains(filename)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }

            byte[] fileContent = fileStorage.getContentBySHA1(fileSha1, filename);
            changeFileContentInWorkingDirectory(filename, fileContent);
        }

        // delete all the untracked file
        List<String> untrackedFile = getUntrackedFileByCommitSha1(commitId);
        for (String untrackedFilename : untrackedFile) {
            File untrackedFileToDelete = Utils.join(CWD, untrackedFilename);
            Utils.restrictedDelete(untrackedFileToDelete);
        }

        // clear the staging area
        StagingArea.clearStagingArea();
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
}
