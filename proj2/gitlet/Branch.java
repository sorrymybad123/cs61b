package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;


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




    public Branch(String name, String sha1) throws IOException {
        this.name = name;
        this.commitSha1 = sha1;
       // create the file
       createBranchFile();
       // make sha1 into the file
       writeSha1ToBranchFile(sha1);
    }

    /**
     * get commit by branch name
     */
    public static Commit getCommitByBranchName(String branchName) {
        return Commit.findCommitByCommitSha1(Branch.checkupCommitIdByBranchName(branchName));
    }

    /**
     * find main branch
     */
    public static File findMainBranch() {
        String pathBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = new File(pathBranch);
        return branchFile;
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
     * get current branches
     */
    public static List<String> getCurrentBranchesO() {
        return Utils.plainFilenamesIn(branches);
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
