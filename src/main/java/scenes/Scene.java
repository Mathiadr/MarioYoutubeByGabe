package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Animator;
import components.Component;
import components.ComponentDeserializer;
import brunostEngine.*;
import components.SpriteRenderer;
import org.joml.Vector2f;
import physics2d.Physics2D;
import renderer.Renderer;
import util.ResourcePool;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {
    private String fileName;
    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private List<GameObject> disabledGameObjects;
    private List<GameObject> pendingObjects;
    private Physics2D physics2D;

    private SceneBuilder sceneBuilder;

    public GameObject lastAddedObject;

    public Scene(SceneBuilder sceneBuilder) {
        this.sceneBuilder = sceneBuilder;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.pendingObjects = new ArrayList<>();
        this.isRunning = false;
        this.fileName = this.sceneBuilder.assignTitleToScene();
    }

    public Physics2D getPhysics() {
        return this.physics2D;
    }

    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneBuilder.loadResources(this);
        for (GameObject g : getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(ResourcePool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if (g.getComponent(Animator.class) != null) {
                Animator animator = g.getComponent(Animator.class);
                animator.refreshTextures();
            }
        }
        this.sceneBuilder.init(this);
    }

    public void onStart() {
        if (Tilemap.get() != null)
            Tilemap.get().onStart();
        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.onStart();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            lastAddedObject = go;
            gameObjects.add(go);
        } else {
            pendingObjects.add(go);
        }
    }

    /**
     *
     * @author Mathias Ratdal
     * @param gameObjectToBePlaced
     */
    public void placeRelativeToGameObject(GameObject gameObjectToBePlaced, GameObject relativeToGameObject, Direction direction) {
        if (relativeToGameObject != null) {
            switch (direction){
                case Up -> {
                    gameObjectToBePlaced.transform.position.x = relativeToGameObject.transform.position.x;
                    gameObjectToBePlaced.transform.position.y = relativeToGameObject.transform.position.y + 0.25f;
                    addGameObjectToScene(gameObjectToBePlaced);
                }
                case Down -> {
                    gameObjectToBePlaced.transform.position.x = relativeToGameObject.transform.position.x;
                    gameObjectToBePlaced.transform.position.y = relativeToGameObject.transform.position.y - 0.25f;
                    addGameObjectToScene(gameObjectToBePlaced);
                }
                case Left -> {
                    gameObjectToBePlaced.transform.position.x = relativeToGameObject.transform.position.x - 0.25f;
                    gameObjectToBePlaced.transform.position.y = relativeToGameObject.transform.position.y;
                    addGameObjectToScene(gameObjectToBePlaced);
                }
                case Right -> {
                    gameObjectToBePlaced.transform.position.x = relativeToGameObject.transform.position.x + 0.25f;
                    gameObjectToBePlaced.transform.position.y = relativeToGameObject.transform.position.y;
                    addGameObjectToScene(gameObjectToBePlaced);
                }
            }
        }
    }

    /**
     *
     * @author Mathias Ratdal
     * @param gameObject
     */
    public void placeRelativeToLastPlacement(GameObject gameObject, Direction direction) {
        if (lastAddedObject != null) {
            switch (direction){
                case Up -> {
                    gameObject.transform.position.x = lastAddedObject.transform.position.x;
                    gameObject.transform.position.y = lastAddedObject.transform.position.y + 0.25f;
                    addGameObjectToScene(gameObject);
                }
                case Down -> {
                    gameObject.transform.position.x = lastAddedObject.transform.position.x;
                    gameObject.transform.position.y = lastAddedObject.transform.position.y - 0.25f;
                    addGameObjectToScene(gameObject);
                }
                case Left -> {
                    gameObject.transform.position.x = lastAddedObject.transform.position.x - 0.25f;
                    gameObject.transform.position.y = lastAddedObject.transform.position.y;
                    addGameObjectToScene(gameObject);
                }
                case Right -> {
                    gameObject.transform.position.x = lastAddedObject.transform.position.x + 0.25f;
                    gameObject.transform.position.y = lastAddedObject.transform.position.y;
                    addGameObjectToScene(gameObject);
                }
            }
        } else {
            System.err.println("[Error] Could not place relative to last object place because it is null!");
        }
    }

    public void destroy() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
    }

    public <T extends Component> GameObject getGameObjectWith(Class<T> clazz) {
        for (GameObject go : gameObjects) {
            if (go.getComponent(clazz) != null) {
                return go;
            }
        }

        return null;
    }

    public <T extends Component> ArrayList<GameObject> getAllGameObjectsWith(Class<T> clazz) {
        ArrayList<GameObject> returnArray = new ArrayList<>();
        for (GameObject go : gameObjects) {
            if (go.getComponent(clazz) != null) {
                returnArray.add(go);
            }
        }

        return returnArray;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        if (result.orElse(null) == null)
            System.err.println("Could not find GameObject with ID " + gameObjectId);
        else System.out.println("Got GameObject("+gameObjectId+"): " + result.orElse(null));
        return result.orElse(null);
    }

    public void editorUpdate(float dt) {
        this.camera.adjustProjection();

        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects) {
            gameObjects.add(go);
            go.onStart();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingObjects.clear();
    }

    public GameObject getGameObject(String gameObjectName) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.name.equals(gameObjectName))
                .findFirst();
        return result.orElse(null);
    }

    public void onUpdate(float dt) {
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for (int i=0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            if (go.isEnabled())
                go.onUpdate(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects) {
            gameObjects.add(go);
            go.onStart();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingObjects.clear();
    }

    public void render() {
        this.renderer.render();
    }

    public Camera camera() {
        return this.camera;
    }

    public void imgui() {
        this.sceneBuilder.imgui();
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(fileName).append(".txt");
            System.out.println("Saving to " + stringBuilder.toString());
            FileWriter writer = new FileWriter(stringBuilder.toString());
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        String inFile = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(fileName).append(".txt");
            System.out.println("loading from " + stringBuilder.toString());
            try {
                inFile = new String(Files.readAllBytes(Paths.get(stringBuilder.toString())));
            }catch (NoSuchFileException e){
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }
}
