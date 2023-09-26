package gitlet;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0); // exit the program
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.initFile();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                checkInitOrNot();
                validateNumArgs("add", args, 2);
                Repository.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                checkInitOrNot();
                if (args.length != 2) {
                    System.out.println("Please input a commit message.");
                }
                Branch master = new Branch("master");
                Repository.commit(args[1], master);
                break;
            case "checkout":
                // TODO
                break;
            default:
                System.out.println("No command with that name exists!");
                System.exit(0); // exit the program
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
