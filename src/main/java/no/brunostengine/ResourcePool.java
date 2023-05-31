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
import java.util.jar.JarFile;

public class ResourcePool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    /**
     * I know. <b>I know</b>. This is probably the most scuffed way to do this, but I've worked on this for WEEKS over
     * entire days and nights trying to figure out how to make it work inside a .jar file, but unfortunately nothing worked.
     *
     */
    static void createShaderIfDoesNotExist(File file, String filepath){
        try {
            if(file.createNewFile()){
                System.out.println("Could not find shader file. Creating new shader file...");
                BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
                String filename = file.getName();
                if (filename.equals("default.glsl"))
                    writer.write(ShaderLoader.defaultShader);
                else if (filename.equals("debugLine2D.glsl"))
                    writer.write(ShaderLoader.debugShader);
                else if (filename.equals("pickingShader.glsl"))
                    writer.write(ShaderLoader.pickingShader);
                else {
                    writer.close();
                    try {
                        file.deleteOnExit();
                    } catch (SecurityException e) {
                        System.err.println("Could not delete unneccessary file due to security manager denying access");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        System.out.println("Loading shader file " + file.getName());
        createShaderIfDoesNotExist(file, resourceName);
        if (ResourcePool.shaders.containsKey(file.getAbsolutePath())) {
            return ResourcePool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            System.out.println("Shader finished compiling.");
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
