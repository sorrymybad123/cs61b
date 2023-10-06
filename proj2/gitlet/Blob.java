package gitlet;


import java.io.*;
import java.util.LinkedList;

public class Blob implements Serializable {
    private byte[] content;
    private String sha1;

    private String filename;

    /**
     *
     */
    public Blob(File file) throws IOException {
        this.content = readContentFromFile(file);
        this.sha1 = getSha1ByFile(file);
        this.filename = file.getName();
        addBlobToStorage();
    }

    public Blob(String filename, String fileSha1,  byte[] content) {
        this.filename = filename;
        this.sha1 = fileSha1;
        this.content = content;
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
    private void addBlobToStorage() throws IOException {
        FileStorage fileStorage = new FileStorage();
        fileStorage.addFile(sha1, filename, content);
    }

    /**
     * get blob by sha1
     */
    public Blob getBlobBySha1(String sha1) {
        if (sha1.equals(this.getSha1())) {
            return this;
        }
        return null;
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
            blobLinkedList = StagingArea.getBlobListForThisStagingArea();
        }

        if (!StagingArea.stagingArea.exists()) {
            StagingArea.stagingArea.createNewFile();
        }
        // add this to link list
        blobLinkedList.add(this);

        // write the merged blob back to the staging area file
        Utils.writeObject(StagingArea.stagingArea, blobLinkedList);
    }

    /*
     * Serialize a single Blob to the staging area

    public void saveBlob() throws IOException {
        // Check if the staging area file exists
        if (!StagingArea.stagingArea.exists()) {
            StagingArea.stagingArea.createNewFile();
        }

        // Write the current Blob to the staging area file
        Utils.writeObject(StagingArea.stagingArea, this);
    }
    */



    /**
     * get sha1 by file content
     */
    public static String getSha1ByFile(File file) throws IOException {
        return calculateSHA1(readContentFromFile(file));
    }

    /**
     * reading content from file
     */
    private static byte[] readContentFromFile(File file) {
        if (!file.exists()) {
            System.out.println("this file do not exist in readContentFile() function");
            System.exit(0);
        }
        return Utils.readContents(file);
    }

    /**
     * calculate sha1 of file
     */
    private static String calculateSHA1 ( byte[] content) {
        return Utils.sha1(content);
    }
}
