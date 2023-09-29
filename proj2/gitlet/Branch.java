package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * use branches file to store all branch and the content of file is sha1
 */
public class Branch {

    static public File branches = Utils.join(Repository.GITLET_DIR, "branches");

    // use 1 2 3 display three status
    static public final int status_HEAD = 1;
    static public final int status_main = 2;
    static public final int status_other = 3;


    // Branch name
    String name;

    // which sha1 of commit
    String commitSha1;

    int status;



    public Branch(String name,String sha1) throws IOException {
        this.name = name;
        this.commitSha1 = sha1;
       // create the file
       createBranchFile();
       // make sha1 into the file
       writeSha1ToBranchFile(sha1);
    }

    /**
     * find main branch
     */
    public static File findMainBranch() {
        String pathBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = new File(pathBranch);
        return branchFile;
    }

    /**
     * put this branch as main branch
     * @throws IOException
     */
    public void changeStatusToMain() {
        this.status = status_main;
    }

    /**
     * make this branch is HEAD, HEAD only have one
     * @throws IOException
     */
    public void statusAsHEAD() {
        this.status = status_HEAD;
    }

    /**
     * change status to normal branch which does not chase
     * @throws IOException
     */
    public void changeStatusToNormalBranch() {
        this.status = status_other;
    }

    public Branch(String name) throws IOException {
        this.name = name;
        // create the file
        createBranchFile();
    }



    /**
     * create a file by the branch name
     */
    public void createBranchFile() throws IOException {
        String name = this.getName();
        File nameOfBranch = Utils.join(branches, name);
        nameOfBranch.createNewFile();
    }

    /**
     * write the sha1 into the branch file
     */
    public void writeSha1ToBranchFile(String sha1) throws IOException {
        File file = Utils.join(branches, getName());
        if (!file.exists()) {
            file.createNewFile();
        }
        Utils.writeContents(file, sha1);
    }

    /**
     * return name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * return teh commit sha1
     * @return
     */
    public String getSha1() {
        return commitSha1;
    }

    /**
     * update the file by the sha1
     * @param commitSha1Code
     */
    public void upDateBranch(String commitSha1Code) throws IOException {
        writeSha1ToBranchFile(commitSha1Code);
    }

    /**
     * check up a branch
     */
    public static String checkupCommitIdByBranchName(String BranchName) {
        // check up the branch file
        File Branchfile = checkupBranchFile(BranchName);

        // get the sha1 of commit
        String commitSha1 = Utils.readContentsAsString(Branchfile);

        return commitSha1;
    }


    /**
     * check up the branch file by name
     */
    public static File checkupBranchFile(String BranchName) {
        File file = Utils.join(branches, BranchName);
        if (!file.exists()) {
            return null;
        }
        return file;
    }
}
