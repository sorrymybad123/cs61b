package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileStorage {
    private HashMap<String, Map<String, byte[]>> fileMap;

    static public File fileStorage = Utils.join(Repository.GITLET_DIR, "storage");

    public FileStorage() {
        fileMap = new HashMap<>();
    }

    /**
     * add a sha1 value and file name
     * @param sha1
     * @param filename
     * @param
     */
    public void addFile(String sha1, String filename, byte[] content) {
        if (!(sha1 != null && filename != null && content != null)) {
            System.out.println("addFile parameters can not be null");
            System.exit(0);
        }
        // make map of the file into latest fileMap
        if (fileStorage.exists()) {
            fileMap = loadFileMap();
        }
        // if sha1 does not exist in outside hashMap, create a inner hashMap to store file
        if (!fileMap.containsKey(sha1)) {
            fileMap.put(sha1, new HashMap<>());
        }

        // make file name and the content of file add to inner hashMap
        fileMap.get(sha1).put(filename, content);
        // store the resent add into fileMap
        saveFileMap(fileMap);
    }

    /**
     * use sha1 to obtain the content
     * @param sha1
     * @param fileName
     * @return
     */
    public byte[] getContentBySHA1(String sha1, String fileName) {
        if (!fileMap.containsKey(sha1)) {
            System.out.println("there is no such sha1 in storage");
            System.exit(0);
        }
        return fileMap.get(sha1).get(fileName);
    }


    /**
     * store map to Storage
     * @param map
     */
    private void saveFileMap(HashMap<String, Map<String, byte[]>> map) {
        Utils.writeObject(fileStorage, map);
    }

    /**
     * load fileMap from file
      */
    private HashMap loadFileMap() {
        HashMap<String, Map<String, byte[]>> hashMap = (HashMap<String, Map<String,byte[]>>) Utils.readObject(fileStorage,HashMap.class);
        return hashMap;
    }


}
