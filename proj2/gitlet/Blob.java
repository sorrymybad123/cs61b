package gitlet;


import java.io.IOException;
import java.io.File;
import java.io.Serializable;

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
    private void addBlobToStorage(File file) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.addFile(sha1, filename, content);
    }

    /**
     * serialize a blob in Staging area
     */
    public void savaBlob() throws IOException {
        if (!StagingArea.stagingArea.exists()) {
            StagingArea.stagingArea.createNewFile();
        }
        Utils.writeObject(StagingArea.stagingArea, this);
    }

    /**
     * reading content from file
     */
    private byte[] readContentFromFile(File file) throws IOException {
        if (!file.exists()) {
            System.out.println("this file do not exist in readContentFile() function");
            System.exit(0);
        }
        return Utils.readContents(file);
    }

    /**
     * calculate sha1 of file
     */
    private String calculateSHA1(byte[] content) {
        return Utils.sha1(content);
    }


}
