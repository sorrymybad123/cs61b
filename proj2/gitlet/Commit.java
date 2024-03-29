package gitlet;

// TODO: any imports you need here

import org.w3c.dom.Node;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.util.logging.SimpleFormatter;

/** Represents a gitlet commit object.
 *  when the command line type commit
 *  we can create a commit structure to commit something
 *  does at a high level.
 *
 *  @lzh TODO
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */

    static public File commitsFile = Utils.join(Repository.GITLET_DIR, "commitsFile");
    static public File commitFile = Utils.join(Repository.GITLET_DIR, "commitFile");
    private String message;
    private Date date;
    private String parent;
    // second parent for merge?
    private String secondParent;


    // sha1 code to point storage file
    private TreeMap<String, String> blobPoints;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");

    private String MergeMessage;

    public Commit(String message, String parent, String secondParent) throws IOException {
        this(message, parent);
        this.secondParent = secondParent;
        MergeMessage = parent.substring(0, 7) + " " + secondParent.substring(0, 7);
    }

    public Commit() throws IOException {
        this.parent = null;
        this.secondParent = null;
        this.message = "initial commit";
        date = new Date(0);
        blobPoints = new TreeMap<>();
    }

    public Commit(String message, String parent) throws IOException {
        // Initialize blobPoints with an empty TreeMap
        this.blobPoints = new TreeMap<>();

        // get parent map
        if (parent != null) {
            Commit lastCommit = deserializeCommitBySha1(parent);
            // update last points to this points
            TreeMap<String, String> lastBlobPoints = lastCommit.getBlobPoints();
            // if last blob points is null
            if (lastBlobPoints != null) {
                this.combineTwoMap(lastBlobPoints);
            }
            // if rmStaging Area is existing delete it from new commit
            if (StagingArea.rmStagingArea.exists()) {
                LinkedList<String> rmFileName = getListOfNameFromRmArea();
                updateDeletion(rmFileName);
                // after the deletion delete the rm area
                StagingArea.clearRmArea();
            }
        }

        // constructor
        this.message = message;
        this.parent = parent;
        this.secondParent = null; // Set secondParent to null
        this.date = new Date();

        // if parent is not null
        if (StagingArea.stagingArea.exists()) {
            // get the new map
            Map<String, String> newMap = getMapFromStagingArea();
            // update this map to blobPoints
            updatePoints(newMap);
            // clear the staging area
            StagingArea.clearStagingArea();
        }

    }



    /**
     * find latest branch commit
     */
    public static Commit findLatestBranchCommit() {
        return findCommitByCommitSha1(findLatestCommitSha1());
    }


    /**
     * combine a treeMap by another treeMap
     */
    private void combineTwoMap(TreeMap<String, String> treeMap) {
        for (String x : treeMap.keySet()) {
            this.blobPoints.put(x, treeMap.get(x));
        }
    }

    /**
     * get sha1 of this commit
     */
    public String getSha1OfCommit() throws IOException {
        return Utils.sha1(this.getContentOfFileToGetSha1());
    }

    /**
     * get the content of file
     * @return
     */
    private byte[] getContentOfFileToGetSha1() throws IOException {
        File file = this.savaCommitToFile();
        byte[] content = Utils.readContents(file);
        return content;
    }


    public String getMessage() {
        return message;
    }

    public Date getDate(){
        return date;
    }

    public String getForMatDate() {
        return dateFormat.format(getDate());
    }

    public String getParent() {
        return parent;
    }

    public String getSecondParent() {
        return secondParent;
    }

    /**
     * save(serialize) this commit as a new file and the filename is the sha1 of this commit
     */
    public void savaCommit() throws IOException {
        String commitName = this.getSha1OfCommit();
        File file = Utils.join(commitsFile, commitName);
        file.createNewFile();
        Utils.writeObject(file, this);
    }

    /**
     * get merged message
     */
    public String getMergeMessage() {
        return MergeMessage;
    }

    /**
     * find latest commit
     */
    public static Commit findLatestCommit() {
        return findCommitByCommitSha1(findLatestCommitSha1());
    }


    /**
     * get current file pointer from current commit
     * @return
     * @throws IOException
     */
    public static TreeMap<String, String> getCurrentCommitBlobPoints() {
        Commit commit = findLatestCommit();
        return commit.getBlobPoints();
    }

    public File savaCommitToFile() throws IOException {
        commitFile.createNewFile();
        Utils.writeObject(commitFile, this);
        return commitFile;
    }

    /**
     * get points from commit
     */
    public TreeMap<String, String> getBlobPoints() {
        return blobPoints;
    }

    /**
     * deserialize this commit
     */
    public static Commit deserializeCommitBySha1(String sha1) {
        File file = Utils.join(commitsFile, sha1);
        if (!file.exists()) {
            return null;
        }
        return Utils.readObject(file, Commit.class);
    }

    /**
     * get the name sha1 map info from staging area
     */
    private Map<String, String> getMapFromStagingArea() {
        if (!StagingArea.stagingArea.exists()) {
            System.out.println("No change to commit");
            System.exit(0);
        }
        StagingArea stagingArea = new StagingArea(true);
        return stagingArea.loadStagingAreaToMap();
    }

    /**
     *rm the name info from rm staging area
     */
    private LinkedList<String> getListOfNameFromRmArea() {
        if (!StagingArea.rmStagingArea.exists()) {
            return null;
        }
        return Utils.readObject(StagingArea.rmStagingArea, LinkedList.class);
    }

    /**
     * find the latest commit sha1
     */
    public static String findLatestCommitSha1() {
        File file = Branch.findMainBranch();
        if (!file.exists()) {
            return null;
        }
        String HEAD = Utils.readContentsAsString(file);
        return HEAD;
    }


    /**
     * use sha1 to find the commit
     */
    public static Commit findCommitByCommitSha1(String commitSha1) {
        if (commitSha1 == null) {
            return null;
        }
        File file = Utils.join(commitsFile, commitSha1);
        return  Utils.readObject(file, Commit.class);
    }

    /**
     *if the file in points display true not in points return false
     */
    public boolean checkTheFileInBlobPointsOrNot(String filename) {
        for (String x : this.getBlobPoints().keySet()) {
            if (x.equals(filename)) {
                return true;
            }
        }
        return false;
    }


    /**
     * find the parent commit
     */
    public Commit getParentCommit() {
        return Commit.findCommitByCommitSha1(getParent());
    }

    /**
     * find second parent commit
     */
    public Commit getSecondParentCommit() {
       return Commit.findCommitByCommitSha1(getSecondParent());
    }

    private void getWholeBranchOfThisCommitHelper(LinkedList<String> branchList) throws IOException {
        branchList.add(this.getSha1OfCommit());
        if (this.parent != null) {
            Commit commitParent = this.getParentCommit();
            commitParent.getWholeBranchOfThisCommitHelper(branchList);
        }
        if (this.secondParent != null) {
            Commit commitSecondParent = this.getSecondParentCommit();
            commitSecondParent.getWholeBranchOfThisCommitHelper(branchList);
        }
    }

    /**
     * get the whole branch of this commit
     */
    public LinkedList<String> getWholeBranchOfThisCommit() throws IOException {
        LinkedList<String> linkedListOfWholeBranch = new LinkedList<>();
        getWholeBranchOfThisCommitHelper(linkedListOfWholeBranch);
        return linkedListOfWholeBranch;
    }



    /**
     * find the latest same parent
     */
    public Commit findTheSplitPoint(String branchName) throws IOException {
        Commit commitGivenBranch = Branch.getCommitByBranchName(branchName);
        LinkedList<String> wayOfGivenBranch = commitGivenBranch.getWholeBranchOfThisCommit();
        LinkedList<String> wayOfCurrentBranch = this.getWholeBranchOfThisCommit();
        for (String x :wayOfGivenBranch) {
            if (wayOfCurrentBranch.contains(x)) {
                return Commit.findCommitByCommitSha1(x);
            }
        }
        return null;
    }

    /**
     * update deletion in new commit
     */
    private void updateDeletion(LinkedList<String> filenamesToDeletion) {
        for (String filenameToDeletion : filenamesToDeletion) {
            if (checkTheFileInBlobPointsOrNot(filenameToDeletion)) {
                this.blobPoints.remove(filenameToDeletion);
            }
        }
    }


    /**
     * update map to commit treemap
     */
    private void updatePoints(Map<String, String> map) {
        for (String x : map.keySet()) {
            this.blobPoints.put(x, map.get(x));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Commit) {
            Commit oas = (Commit) o;
            try {
                if (this.getSha1OfCommit().equals(oas.getSha1OfCommit())) {
                    return true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;

    }
}
