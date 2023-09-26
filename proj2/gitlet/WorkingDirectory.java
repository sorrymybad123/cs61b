package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkingDirectory {

    public WorkingDirectory(){
    }
    /**
     * use list to store files in working direction
     * @return
     */
    public static List<File> listFile() {
        List<String> workingFileName = Utils.plainFilenamesIn(Repository.CWD);
        if (workingFileName == null) {
            return null;
        }
        List<File> workingFiles = new ArrayList<>();
        for (String x : workingFileName) {
            File file = Utils.join(Repository.CWD, x);
            workingFiles.add(file);
        }
        return workingFiles;
    }

    /**
     * obtain file from working direction by file name
     */
    public static File getWorkingDirFile(String name) {
        File file = Utils.join(Repository.CWD, name);
        if (!file.exists()) {
            System.out.println("working directory do not have this file");
            System.exit(0);
        }
        return file;
    }
}
