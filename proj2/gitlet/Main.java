package gitlet;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Please enter a command");
                System.exit(0); // exit the program
            }
            String firstArg = args[0];
            switch (firstArg) {
                case "init":
                    validateNumArgs("init", args, 1);
                    if (Repository.GITLET_DIR.exists()) {
                        System.out.println("A Gitlet version-control system already exists in the current directory.");
                        break;
                    }
                    Repository.initFile();
                    break;
                case "add":
                    checkInitOrNot();
                    validateNumArgs("add", args, 2);
                    Repository.add(args[1]);
                    break;
                case "commit":
                    checkInitOrNot();
                    if (args.length != 2 || args[1].equals("")) {
                        System.out.println("Please enter a commit message.");
                        System.exit(0);
                    }
                    Repository.commit(args[1]);
                    break;
                case "checkout":
                    checkInitOrNot();
                    // first circumstance -- file name
                    if (args.length == 3) {
                        if (!args[1].equals("--")) {
                            Utils.exit("Incorrect operands.");
                        }
                        validateNumArgs("checkout", args, 3);
                        Repository.checkout(Commit.findLatestCommitSha1(), args[2]);
                    } else if (args.length == 4) {
                        if (!args[2].equals("--")) {
                            Utils.exit("Incorrect operands.");
                        }
                        validateNumArgs("checkout", args, 4);
                        Repository.checkout(args[1], args[3]);
                    } else if (args.length == 2) {
                        validateNumArgs("checkout", args, 2);
                        Repository.checkout(args[1]);
                    } else {
                        System.out.println("Incorrect operands.");
                    }
                    break;
                case "rm":
                    checkInitOrNot();
                    validateNumArgs("rm", args, 2);
                    Repository.rm(args[1]);
                    break;
                case "log":
                    checkInitOrNot();
                    validateNumArgs("log", args, 1);
                    Repository.log();
                    break;
                case "global-log":
                    checkInitOrNot();
                    validateNumArgs("global-log", args, 1);
                    Repository.globalLog();
                    break;
                case "find":
                    checkInitOrNot();
                    validateNumArgs("find", args, 2);
                    Repository.find(args[1]);
                    break;
                case "status":
                    checkInitOrNot();
                    validateNumArgs("status", args, 1);
                    Repository.status();
                    break;
                case "branch":
                    checkInitOrNot();
                    validateNumArgs("branch", args, 2);
                    Repository.createBranch(args[1]);
                    break;
                case "rm-branch":
                    checkInitOrNot();
                    validateNumArgs("rm-branch", args, 2);
                    Repository.deleteBranch(args[1]);
                    break;
                case "reset":
                    checkInitOrNot();
                    validateNumArgs("reset", args, 2);
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    checkInitOrNot();
                    validateNumArgs("merge", args, 2);
                    Repository.merge(args[1]);
                    break;
                default:
                    System.out.println("No command with that name exists!");
                    System.exit(0); // exit the program
            }

        } catch (IOException exception)  {
            System.exit(0);
        }

    }

    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void checkInitOrNot() {
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
