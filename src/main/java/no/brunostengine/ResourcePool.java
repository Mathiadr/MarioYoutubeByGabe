package no.brunostengine;

import no.brunostengine.renderer.ResourceReader;
import no.brunostengine.renderer.Shader;
import no.brunostengine.renderer.Texture;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResourcePool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getInternalLibraryShaders(String resourceName){
        try {
            //Path path = Paths.get(resourceName);
            //String filename = Path.of(resourceName).getFileName().toString();
            System.out.println(resourceName);
            File tempFile = new File(resourceName);
            System.out.println(tempFile);
            System.out.println(tempFile.getAbsolutePath());
            InputStream is = ResourceReader.GetInputStreamFromResource(resourceName);
            try (FileOutputStream out = new FileOutputStream(tempFile, false)){
                int read;
                byte[] bytes = new byte[8192];
                while ((read = is.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            }
            return getShader(resourceName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Shader getShader(String resourceName) {
        System.out.println(resourceName);
        File file = new File(resourceName);
        System.out.println(file.getAbsolutePath());
        if (ResourcePool.shaders.containsKey(file.getAbsolutePath())) {
            return ResourcePool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            ResourcePool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (ResourcePool.textures.containsKey(file.getAbsolutePath())) {
            return ResourcePool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(resourceName);
            ResourcePool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        if (!ResourcePool.spritesheets.containsKey(file.getAbsolutePath())) {
            ResourcePool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (!ResourcePool.spritesheets.containsKey(file.getAbsolutePath())) {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool.";
        }
        return ResourcePool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
    
    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            assert false : "Sound file not added '" + soundFile + "'";
        }

        return null;
    }

    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            ResourcePool.sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }
}
