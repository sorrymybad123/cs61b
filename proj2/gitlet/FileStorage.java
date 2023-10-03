package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FileStorage implements Serializable{
    private  HashMap<String, Map<String, byte[]>> fileMap = new HashMap<>();

    static public File fileStorage = Utils.join(Repository.GITLET_DIR, "storage");

    public FileStorage() {
    }


    /**
     * add a sha1 value and file name
     * @param sha1
     * @param filename
     * @param
     */
    public void addFile(String sha1, String filename, byte[] content) throws IOException {
        if (sha1 == null || filename == null || content == null) {
            System.out.println("addFile parameters can not be null");
            System.exit(0);
        }
        // make map of the file into latest fileMap
        if (fileStorage.exists()) {
           FileStorage fileStorage1 = loadFileStorage();
           this.fileMap = fileStorage1.getFileMap();
        }

        // if sha1 does not exist in outside hashMap, create a inner hashMap to store file
        if (!fileMap.containsKey(sha1)) {
            fileMap.put(sha1,  new HashMap<String, byte[]>());
        }

        // make file name and the content of file add to inner hashMap
        fileMap.get(sha1).put(filename, content);


        // store the resent add into fileMap
        this.saveFileStorage();
    }




    /**
     * use sha1 to obtain the content
     * @param sha1
     * @return
     */
    public byte[] getContentBySHA1(String sha1) {
        if (sha1 == null) {
            return null;
        }
        if (!fileMap.containsKey(sha1)) {
            System.out.println("there is no such sha1 in storage");
            System.exit(0);
        }
        byte[] result = null;
        Map<String, byte[]> getContentByName = fileMap.get(sha1);
        for (String fileName : getContentByName.keySet()) {
            result = getContentByName.get(fileName);
        }
        return result;
    }

    public HashMap<String ,Map<String, byte[]>> getFileMap() {
        return fileMap;
    }

    /**
     * create a file by file sha1 id
     */
    public static void createFileBySha1(String fileSha1) {
        FileStorage fileStorage1 = FileStorage.loadFileStorage();
        fileStorage1.getContentBySHA1(fileSha1);
    }


    /**
     * create the storage
     */
    private void createStorage() throws IOException {
        fileStorage.createNewFile();
    }

    /**
     * get file name by sha1 id
     */
    public String getNameByFileId(String fileId) {
        String result = null;
        Map<String, byte[]> getContentByName = fileMap.get(fileId);
        for (String fileName : getContentByName.keySet()) {
            result = fileName;
        }
        return result;
    }

    /**
     * store Storage
     */
    private void saveFileStorage() throws IOException {
        if (!fileStorage.exists()) {
            this.createStorage();
        }
        Utils.writeObject(fileStorage,  this);
    }

    /**
     * load fileStorage from file
      */
    public static FileStorage loadFileStorage() {
        return Utils.readObject(fileStorage, FileStorage.class);
    }


}
