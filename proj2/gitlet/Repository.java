package gitlet;

import java.io.File;
import java.io.IOException;

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


    /* TODO: fill in the rest of this class. */
    public static void setPersistent() throws IOException {
        // create the gitLet dir
        GITLET_DIR.mkdir();
        Branch.branches.mkdir();
        Commit.commitsFile.mkdir();
        FileStorage.fileStorage.createNewFile();
    }


    /**
     * init file
     * @throws IOException
     */
    public static void initFile() throws IOException {
        setPersistent();
        Commit initCommit = new Commit();
        initCommit.savaCommit();
        Branch master = new Branch("master", initCommit.getSha1OfCommit());
        Branch HEAD = new Branch("HEAD", initCommit.getSha1OfCommit());
    }

    /**
     * add file to the StagingArea
     */
    public static void add(String filename) throws IOException {
        StagingArea stagingArea = new StagingArea();
        stagingArea.gitLetAdd(filename);
    }

    /**
     * commit from staging area
     */
    public static void commit(String message, Branch branch) throws IOException {
        File file = Utils.join(Branch.branches, "HEAD");

        // use HEAD to find last branch
        String HEAD = Utils.readContentsAsString(file);

        // create a new branch
        Commit commit = new Commit(message, HEAD);

        // update the HEAD branch to this commit
        Branch HeadBranch = new Branch("HEAD", commit.getSha1OfCommit());

        // update the master or other branch of this commit
        branch.upDateBranch(commit.getSha1OfCommit());
    }

    public static void rm() {

    }
}
