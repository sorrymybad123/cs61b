package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * use branches file to store all branch and the content of file is sha1
 */
public class Branch {

    static public File branches = Utils.join(Repository.GITLET_DIR, "branches");


    // Branch name
    String name;

    // which sha1 of commit
    String commitSha1;



    public Branch(String name,String sha1) throws IOException {
        this.name = name;
        this.commitSha1 = sha1;
       // create the file
       createBranchFile();
       // make sha1 into the file
       writeSha1ToBranchFile(sha1);
    }

    public Branch(String name) throws IOException {
        new Branch(name, null);

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
    public void writeSha1ToBranchFile(String sha1) {
        File file = Utils.join(branches, getName());
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
    public void upDateBranch(String commitSha1Code) {
        writeSha1ToBranchFile(commitSha1Code);
    }

    /**
     * check up a branch
     */
    public static String checkupByBranchName(String BranchName) {
        // check up the branch file
        File Branchfile = checkupBranchFile(BranchName);

        // get the sha1 of commit
        String commitSha1 = Utils.readContentsAsString(Branchfile);

        return commitSha1;
    }

    /**
     * check up the branch file by name
     */
    private static File checkupBranchFile(String BranchName) {
        File file = Utils.join(branches, BranchName);
        if (!file.exists()) {
            System.exit(0);
        }
        return file;
    }
}
