package no.brunostengine;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;

public class AudioHandler {
    private static HashMap<String, String> sounds = new HashMap<>();

    public static void play(String filename){
        try {
            String pathToFile = sounds.get(filename);
            ResourcePool.getSound(pathToFile).play();
        } catch (NullPointerException e){
            System.err.println("Could not find sound file \"" + filename + "\"");
            e.printStackTrace();
        }
    }

    public static void add(String pathToFile, boolean loops){
        ResourcePool.addSound(pathToFile, loops);
        String filename = Path.of(pathToFile).getFileName().toString();
        sounds.put(filename, pathToFile);
    }

    public static Sound get(String filename){
        try {
            String pathToFile = sounds.get(filename);
            return ResourcePool.getSound(pathToFile);
        } catch (NullPointerException e) {
            System.err.println("Could not find sound file \"" + filename + "\"");
            e.printStackTrace();
        }
        return null;
    }

    public static Collection<Sound> getAllSounds(){
        return ResourcePool.getAllSounds();
    }
}
