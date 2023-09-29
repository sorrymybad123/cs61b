package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StagingArea {
    static public File stagingArea = Utils.join(Repository.GITLET_DIR, "stagingArea");

    static public File rmStagingArea = Utils.join(Repository.GITLET_DIR, "rmStagingArea");


    private boolean rmOrAdd; // true is add, false is deletion

    public StagingArea(boolean rmOrAdd) {
        this.rmOrAdd = rmOrAdd;
    }

    /**
     * add file to staging area
     * @param name
     */
    public void gitLetAdd(String name) throws IOException {
        // get the file
        File fileToAdd = Utils.join(Repository.CWD, name);

        // TODO: if the content of file is same with the content of HEAD commit we should not add to staging area again
        String addSha1 = Blob.getSha1ByFile(fileToAdd);

        // TODO: if add sha1 is equal to staging area file
        if (!fileToAdd.exists()) {
            Utils.error("can't find add file", this);
            System.exit(0);
        }

        // create a blob and store it in FileStorage
        Blob a = new Blob(fileToAdd);

        // store the blob in Staging area
        a.savaBlobToLinkListStagingArea();
    }


    /**
     * remove file from Staging area
     */
    public void rmNameIntoRmArea(String name) throws IOException {
        // TODO: if this file in Staging area then remove it
        if (stagingArea.exists()) {
            // if stagingArea have the file
            if (this.loadStagingAreaToMap().containsKey(name)) {
                // delete that file
                clearStagingArea();
            }
        }
        //store the file in rm area
        storeInRmArea(name);
    }


    /**
     * store file name to rmArea
     */
    private static void storeInRmArea(String filenameToRm) throws IOException {
        // create the rmArea
        rmStagingArea.createNewFile();

        // save the filename to rm staging area
        Utils.writeContents(rmStagingArea, filenameToRm);
    }

    /**
     * clear rmArea
     */
    public static void clearRmArea() {
        if (rmStagingArea.exists()) {
            rmStagingArea.delete();
        }
    }



    /**
     * clear staging area
     */
    public static void clearStagingArea() {
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
        LinkedList<Blob> blobLinkedList = Utils.readObject(StagingArea.stagingArea, LinkedList.class);


        // create a map and map this two value
        Map<String, String> name_Sha1 = new HashMap<>();

        for (Blob blob : blobLinkedList) {
            String filename = blob.getFilename();
            String sha1 = blob.getSha1();
            name_Sha1.put(filename, sha1);
        }

        return name_Sha1;
    }
}
