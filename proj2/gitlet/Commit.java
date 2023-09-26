package gitlet;

// TODO: any imports you need here

import org.w3c.dom.Node;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.time.LocalDate;

/** Represents a gitlet commit object.
 *  when the command line type commit
 *  we can create a commit structure to commit something
 *  does at a high level.
 *
 *  @lzh TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */

    static public File commitsFile = Utils.join(Repository.GITLET_DIR, "commitsFile");
    private String message;
    private Date date;
    private String parent;
    // second parent for merge?
    private String secondParent;

    // sha1 code to point storage file
    private TreeMap<String, String> blobPoints;


    public Commit(String message, String parent, String secondParent) {
        // get parent map
        if (parent != null) {
            Commit lastCommit = deserializeCommitBySha1(parent);
            // update last points to this points
            TreeMap<String, String> lastBlobPoints = lastCommit.getBlobPoints();
            this.combineTwoMap(lastBlobPoints);
        }

        this.message = message;
        this.parent = parent;
        this.secondParent = secondParent;

        if (message != null) {
            // get the new map
            Map<String, String> newMap = getMapFromStagingArea();
            // update this map to blobPoints
            updatePoints(newMap);
        }
    }

    public Commit(String message, String parent) {
        new Commit(message, parent, null);
    }

    /**
     * combine a treeMap by another treeMap
     */
    private void combineTwoMap(TreeMap<String, String> treeMap) {
        for (String x : treeMap.keySet()) {
            this.blobPoints.put(x, treeMap.get(x));
        }
    }



    public Commit() {
        new Commit("initial commit", null, null);
        date = new Date(0);
        blobPoints = new TreeMap<>();
    }

    /**
     * get sha1 of this commit
     */
    public String getSha1OfCommit() {
        return Utils.sha1(this);
    }

    /**
     * save(serialize) this commit as a new file and the filename is the sha1 of this commit
     */
    public void savaCommit() throws IOException {
        String commitName = getSha1OfCommit();
        File file = Utils.join(commitsFile, commitName);
        file.createNewFile();
        Utils.writeObject(file, this);
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
        StagingArea stagingArea = new StagingArea();
        return stagingArea.loadStagingAreaToMap();
    }

    /**
     * update map to commit treemap
     */
    private void updatePoints(Map<String, String> map) {
        for (String x : map.keySet()) {
            this.blobPoints.put(x, map.get(x));
        }
    }
}
