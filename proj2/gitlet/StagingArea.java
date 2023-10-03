package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class StagingArea implements Serializable {
    static public File stagingArea = Utils.join(Repository.GITLET_DIR, "stagingArea");

    static public File rmStagingArea = Utils.join(Repository.GITLET_DIR, "rmStagingArea");


    private boolean rmOrAdd; // true is add, false is deletion

    public StagingArea(boolean rmOrAdd) {
        this.rmOrAdd = rmOrAdd;
    }

    /**
     * get the Link list of blobs
     */
    public static LinkedList<Blob> getBlobListForThisStagingArea() {
        LinkedList<Blob> blobLinkedList = Utils.readObject(stagingArea, LinkedList.class);
        return blobLinkedList;
    }

    /**
     * delete a blob from staging area by blob sha1
     */
    public static void deleteBlobFromStagingArea(String sha1) {
        LinkedList<Blob> blobLinkedList = StagingArea.getBlobListForThisStagingArea();
        for (Blob b : blobLinkedList) {
            if (b.getBlobBySha1(sha1) != null) {
                Blob deleteBlob = b;
                blobLinkedList.remove(deleteBlob);
            }
        }
        // update this back to staging area
        if (blobLinkedList.equals(new LinkedList<>())) {
            clearStagingArea();
        } else {
            // write this back to staging area
            Utils.writeObject(stagingArea, blobLinkedList);
        }
    }


    /**
     * get rm name from rm area
     */
    public static String getCurrentRmName() {
        return Utils.readContentsAsString(rmStagingArea);
    }

    /**
     * find the linklist of the staging area if it is have the file
     */
    public static boolean ifStagingAreaHaveTheFile(String sha1) {
            LinkedList<Blob> blobLinkedList = getBlobListForThisStagingArea();
            for (Blob b : blobLinkedList) {
                if (b.getSha1().equals(sha1)) {
                    return true;
                }
             }
            return false;
    }

    /**
     * add file to staging area
     * @param name
     */
    public void gitLetAdd(String name) throws IOException {
        // get the file
        File fileToAdd = Utils.join(Repository.CWD, name);
        if (!fileToAdd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        //  if the content of file is same with the content of HEAD commit we should not add to staging area again
        String addSha1 = Blob.getSha1ByFile(fileToAdd);

        //  if add sha1 is equal to staging area file
        if (stagingArea.exists()) {
            LinkedList<Blob> blobLinkedList = Utils.readObject(stagingArea, LinkedList.class);
            for (Blob b : blobLinkedList) {
                if (b.getSha1().equals(addSha1) && b.filename.equals(name)) {
                    System.exit(0);
                }
            }
        }

        // find the latest commit have the same file
        TreeMap<String, String> currentCommitPoints = Commit.getCurrentCommitBlobPoints();
        for (String filename : currentCommitPoints.keySet()) {
            if (filename.equals(name)) {
                // if it is the same file with staging area exit it
                if (currentCommitPoints.get(filename).equals(addSha1)){
                    System.exit(0);
                }
            }
        }





        // create a blob and store it in FileStorage
        Blob a = new Blob(fileToAdd);

        // store the blob in Staging area
        a.savaBlobToLinkListStagingArea();
    }

    /**
     * add form commit id
     */
    public static void addFileByFileId(String fileSha1) throws IOException {
        FileStorage fileStorage = FileStorage.loadFileStorage();
        byte[] content = fileStorage.getContentBySHA1(fileSha1);
        String nameBlob = fileStorage.getNameByFileId(fileSha1);
        Blob blob = new Blob(nameBlob, fileSha1, content);
        blob.savaBlobToLinkListStagingArea();
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
     * load rm area file to list of filenames about to remove
     */
    public static LinkedList<String> loadRmAreaToList() {
        return Utils.readObject(rmStagingArea, LinkedList.class);
    }


    /**
     * store file name to rmArea
     */
    private static void storeInRmArea(String filenameToRm) throws IOException {
        LinkedList<String> linkedListOfRmFilename = new LinkedList<>();
        // create the rmArea
        if (rmStagingArea.exists()) {
            linkedListOfRmFilename = loadRmAreaToList();
        } else {
            rmStagingArea.createNewFile();
        }
        linkedListOfRmFilename.add(filenameToRm);
        // save the filename to rm staging area
        Utils.writeObject(rmStagingArea, linkedListOfRmFilename);
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
