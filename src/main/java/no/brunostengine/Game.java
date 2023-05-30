package no.brunostengine;

import no.brunostengine.renderer.DebugDraw;
import no.brunostengine.renderer.Framebuffer;
import no.brunostengine.renderer.Renderer;
import no.brunostengine.renderer.Shader;
import no.brunostengine.util.PixelToGameObjectReader;
import no.brunostengine.observers.EventSystem;
import no.brunostengine.observers.Observer;
import no.brunostengine.observers.events.Event;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import no.brunostengine.physics.PhysicsHandler;

import java.awt.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The Game class holds the most important and low-level functionality, ensuring everything starts correctly.
 * This is where the very application Window is created and run from.
 * This class contains two important Methods and Properties, such as follows:
 * Changing the current Scene to another Scene, and The property that allows reading which
 * GameObject belongs to a given pixel.
 */
public class Game implements Observer {
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;
    private Framebuffer framebuffer;
    private PixelToGameObjectReader pixelToGameObjectReader;
    private boolean runtimePlaying = true;
    private boolean fullscreen = false;

    private static Game game = null;

    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    private static SceneBuilder currentSceneBuilder;

    private Game() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.width = gd.getDisplayMode().getWidth();
        this.height = gd.getDisplayMode().getHeight();
        this.title = "Brunost Engine";
        EventSystem.addObserver(this);
    }
 
    public static void changeScene(SceneBuilder sceneBuilder) {
        if (currentScene != null) {
            currentScene.destroy();
        }

        currentSceneBuilder = sceneBuilder;
        currentScene = new Scene(sceneBuilder);
        currentScene.load();
        currentScene.init();
        currentScene.onStart();
    }

    public static Game get() {
        if (Game.game == null) {
            Game.game = new Game();
        }

        return Game.game;
    }

    public static PhysicsHandler getPhysics() { return currentScene.getPhysics(); }

    public static Scene getScene() {
        return currentScene;
    }

    public void run() {


        loop();

        // Destroy the audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(String title) {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        this.title = title;

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
        System.out.println("Running LWJGL " + Version.getVersion() + "");
        long monitor = glfwGetPrimaryMonitor();
        // Configure GLFW
        glfwDefaultWindowHints();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_REFRESH_RATE, 60);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, monitor, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Game.setWidth(newWidth);
            Game.setHeight(newHeight);
        });

        glfwSetWindowMonitor(glfwWindow, monitor, 0, 0, width, height, 60);



        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not supported.";
        }

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new Framebuffer(Game.getWidth(), Game.getHeight());
        this.pixelToGameObjectReader = new PixelToGameObjectReader(Game.getWidth(), Game.getHeight());
        glViewport(0, 0, Game.getWidth(), Game.getHeight());


        this.imguiLayer = new ImGuiLayer(glfwWindow);
        this.imguiLayer.initImGui();
    }

    public void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;


        Shader defaultShader = ResourcePool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = ResourcePool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            // Render pass 1. Render to picking texture
            glDisable(GL_BLEND);
            pixelToGameObjectReader.enableWriting();

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pixelToGameObjectReader.disableWriting();
            glEnable(GL_BLEND);

            // Render pass 2. Render actual game
            DebugDraw.beginFrame();

            this.framebuffer.bind();
            Vector4f clearColor = currentScene.camera().clearColor;
            glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                Renderer.bindShader(defaultShader);
                if (runtimePlaying) {
                    currentScene.onUpdate(dt);
                }
                currentScene.render();
                DebugDraw.draw();
            }
            this.framebuffer.unbind();

            this.imguiLayer.onUpdate(dt, currentScene);

            KeyListener.endFrame();
            MouseListener.endFrame();
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImguiLayer() {
        return get().imguiLayer;
    }

    public static SceneBuilder getCurrentSceneBuilder() {
        return currentSceneBuilder;
    }

    public static void setCurrentSceneBuilder(SceneBuilder currentSceneBuilder) {
        Game.currentSceneBuilder = currentSceneBuilder;
    }

    public static PixelToGameObjectReader getPixelToGameObjectReader() {
        return get().pixelToGameObjectReader;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
                this.runtimePlaying = true;
                Game.changeScene(currentSceneBuilder);
                break;
            case GameEngineStopPlay:
                glfwSetWindowShouldClose(glfwWindow, true);
                break;
            case LoadLevel:
                Game.changeScene(currentSceneBuilder);
                break;
            case SaveLevel:
                currentScene.save();
                break;
        }
    }
}
