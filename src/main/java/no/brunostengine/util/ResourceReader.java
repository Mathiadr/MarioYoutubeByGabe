package no.brunostengine.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Experimental. Read files from resource folder. Meant to facilitate initialization of files when converted to a .jar
 */
public class ResourceReader {

    private static ResourceReader instance;

    private ResourceReader(){}

    public static InputStream GetInputStreamFromResource(String filepath){
        ClassLoader classLoader = get().getClass().getClassLoader();

        return classLoader.getResourceAsStream(filepath);
    }

    /**
     * EXPERIMENTAL
     * @param filename
     * @return
     */
    public static String getFileFromResource(String filename){
        InputStream is = GetInputStreamFromResource(filename);

        if (is == null) {
            throw new IllegalArgumentException("File not found! {" + filename + "}");
        } else {
            try (InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            String returnLine;
            while ((line = bufferedReader.readLine()) != null){
                returnLine = line;
                if ((line = bufferedReader.readLine()) == null){
                    File file = new File("");
                    return returnLine;
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    private static ResourceReader get(){
        if (instance == null)
            instance = new ResourceReader();
        return instance;
    }
}
