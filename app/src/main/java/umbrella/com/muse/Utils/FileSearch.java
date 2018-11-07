package umbrella.com.muse.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 7/24/2017.
 */

public class FileSearch {

    /**
     * Search a directory and return a list of all **directories** contained inside
     * @param directory
     * @return
     */
    // Returns a list of absolutePaths to Directories
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);

        // Returns a list of File objects inside "file"
        File[] listfiles = file.listFiles();

        // If a File inside listFiles is a Directory, add its absolute to our array
        for(int i = 0; i < listfiles.length; i++){
            if(listfiles[i].isDirectory()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all **files** contained inside
     * @param directory
     * @return
     */
    // Returns a list of absolutePaths to Files
    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        // If a File inside listFiles is a File, add its absolute path to our array
        for(int i = 0; i < listfiles.length; i++){
            if(listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
