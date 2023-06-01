package no.brunostengine.util;


import no.brunostengine.Sound;
import no.brunostengine.Spritesheet;
import no.brunostengine.renderer.Shader;
import no.brunostengine.renderer.Texture;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResourcePool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String resourceName) {
        try {
            File file = new File(resourceName);
            System.out.println("Loading shader file " + file.getName());
            if (ResourcePool.shaders.containsKey(file.getAbsolutePath())) {
                return ResourcePool.shaders.get(file.getAbsolutePath());
            } else {
                Shader shader = new Shader(resourceName);
                shader.compile();
                System.out.println("Shader finished compiling.");
                ResourcePool.shaders.put(file.getAbsolutePath(), shader);
                return shader;
            }
        } catch (NullPointerException e) {
            System.err.println("Could not find shader file.");
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public static Texture getTexture(String resourceName) {
        try {
            File file = new File(resourceName);
            if (ResourcePool.textures.containsKey(file.getAbsolutePath())) {
                return ResourcePool.textures.get(file.getAbsolutePath());
            } else {
                Texture texture = new Texture();
                texture.init(resourceName);
                ResourcePool.textures.put(file.getAbsolutePath(), texture);
                return texture;
            }
        } catch (NullPointerException e) {
            System.err.println("Resource file " + resourceName + " not found! Are you sure you loaded it in LoadResources?");
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        try {
            File file = new File(resourceName);
            if (!ResourcePool.spritesheets.containsKey(file.getAbsolutePath())) {
                ResourcePool.spritesheets.put(file.getAbsolutePath(), spritesheet);
            }
        } catch (NullPointerException e) {
            System.err.println("Resource file "+resourceName+" not found!");
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        try {
            File file = new File(resourceName);
            if (!ResourcePool.spritesheets.containsKey(file.getAbsolutePath())) {
                assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool.";
            }
            return ResourcePool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
        } catch (NullPointerException e) {
            System.err.println("Resource file "+resourceName+" not found! Are you sure you loaded it in LoadResources?");
            e.printStackTrace();
            throw new NullPointerException();
        }
    }
    
    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String soundFile) {
        try {
            File file = new File(soundFile);
            return sounds.get(file.getAbsolutePath());
        } catch (NullPointerException e) {
            System.err.println("Resource file "+soundFile+" not found! Are you sure you loaded it in LoadResources?");
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public static Sound addSound(String soundFile, boolean loops) {
        try {
            File file = new File(soundFile);
            if (sounds.containsKey(file.getAbsolutePath())) {
                return sounds.get(file.getAbsolutePath());
            } else {
                Sound sound = new Sound(file.getAbsolutePath(), loops);
                ResourcePool.sounds.put(file.getAbsolutePath(), sound);
                return sound;
            }
        } catch (NullPointerException e) {
            System.err.println("Resource file "+soundFile+" not found!");
            e.printStackTrace();
            throw new NullPointerException();
        }
    }
}
