package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StagingArea {
    static public File stagingArea = Utils.join(Repository.GITLET_DIR, "stagingArea");

    /**
     * add file to staging area
     * @param name
     */
    public void gitLetAdd(String name) throws IOException {
        // TODO: if the content of file is same with the content of HEAD commit we should not add to staging area again
        // get the file
        File fileToAdd = WorkingDirectory.getWorkingDirFile(name);

        // create a blob and store it in FileStorage
        Blob a = new Blob(fileToAdd);

        // store the blob in Staging area
        a.savaBlob();
    }

    /**
     * clear staging area
     */
    public void clearStagingArea() {
        if (stagingArea.exists()) {
            stagingArea.delete();
        }
    }

    /**
     * load and get map from Staging area
     */
    public Map<String, String> loadStagingAreaToMap() {
        if (!StagingArea.stagingArea.exists()) {
            return null;
        }
        // get the blob from staging area
        Blob blob = Utils.readObject(StagingArea.stagingArea, Blob.class);

        // get the sha1 and filename from blob
        String sha1 = blob.getSha1();
        String filename = blob.getFilename();

        // create a map and map this two value
        Map<String, String> name_Sha1 = new HashMap<>();
        name_Sha1.put(filename, sha1);
        return name_Sha1;
    }
}
