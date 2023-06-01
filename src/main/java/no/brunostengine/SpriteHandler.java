package no.brunostengine;

import no.brunostengine.util.ResourcePool;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class SpriteHandler {
    private static HashMap<String, String> spritesheets = new HashMap<>();

    public static void add(String pathToFile, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        String filename = Path.of(pathToFile).getFileName().toString();
        spritesheets.put(filename, pathToFile);
        ResourcePool.addSpritesheet(pathToFile,
                new Spritesheet(ResourcePool.getTexture(pathToFile), spriteWidth, spriteHeight, numSprites, spacing));
    }

    public static Spritesheet getSpritesheet(String filename){
        try {
            String pathToFile = spritesheets.get(filename);
            return ResourcePool.getSpritesheet(pathToFile);
        } catch (NullPointerException e) {
            System.err.println("Could not find file \"" + filename + "\"");
            e.printStackTrace();
        }
        return null;
    }

    public static Sprite getSprite(String filename, int spriteIndex){
        try {
            return getSpritesheet(filename).getSprite(spriteIndex);
        } catch (NullPointerException e){
            System.err.println("Could not find specified file " + filename);
            e.printStackTrace();
        }
        return null;
    }

    public static List<Sprite> getAllSprites(String filename){
        try {
            return getSpritesheet(filename).getSprites();
        } catch (NullPointerException e){
            System.err.println("Could not find specified file " + filename);
            e.printStackTrace();
        }
        return null;
    }
}
