package gitlet;


import java.io.*;
import java.util.LinkedList;

public class Blob implements Serializable {
    private byte[] content;
    private String sha1;

    public String filename;

    /**
     *
     */
    public Blob(File file) throws IOException {
       this.content = readContentFromFile(file);
       this.sha1 = calculateSHA1(content);
       this.filename = file.getName();
       addBlobToStorage(file);
    }

    public String getSha1() {
        return sha1;
    }

    public String getFilename() {
        return filename;
    }


    /**
     * add blob to FileStorage
     */
    private void addBlobToStorage(File file) throws IOException {
        FileStorage fileStorage = new FileStorage();
        fileStorage.addFile(sha1, filename, content);
    }

    /**
     * serialize a blob in Staging area
     */
    public void savaBlobToLinkListStagingArea() throws IOException {
        // use link list to store blob
        LinkedList<Blob> blobLinkedList = new LinkedList<>();
        // check if staging area file exists
        if (StagingArea.stagingArea.exists()) {
            // save last staging area blobs
            LinkedList<Blob> existBlobList = Utils.readObject(StagingArea.stagingArea, LinkedList.class);
            for (Blob b : existBlobList) {
                blobLinkedList.add(b);
            }
        }

        if (!StagingArea.stagingArea.exists()) {
            StagingArea.stagingArea.createNewFile();
        }
        // add this to link list
        blobLinkedList.add(this);

        // write the merged blob back to the staging area file
        Utils.writeObject(StagingArea.stagingArea, blobLinkedList);
    }

    /**
     * Serialize a single Blob to the staging area
     */
    public void saveBlob() throws IOException {
        // Check if the staging area file exists
        if (!StagingArea.stagingArea.exists()) {
            StagingArea.stagingArea.createNewFile();
        }

        // Write the current Blob to the staging area file
        Utils.writeObject(StagingArea.stagingArea, this);
    }



    /**
     * get sha1 by file content
     */
    public static String getSha1ByFile(File file) throws IOException {
        return calculateSHA1(readContentFromFile(file));
    }

    /**
     * reading content from file
     */
    private static byte[] readContentFromFile(File file) throws IOException {
        if (!file.exists()) {
            System.out.println("this file do not exist in readContentFile() function");
            System.exit(0);
        }
        return Utils.readContents(file);
    }

    /**
     * calculate sha1 of file
     */
    private static String calculateSHA1 ( byte[] content){
        return Utils.sha1(content);
    }
}
