package brunostengine;

import components.Component;

import java.util.LinkedList;
import java.util.Queue;

// https://www.youtube.com/watch?v=fsDE_mO4RZM
public class ObjectPool extends Component {
    private PoolableObject poolableObject;
    private int size;
    private Queue<PoolableObject> availableObjectsPool;

    private ObjectPool(PoolableObject gameObjectToPool, int size){
        this.poolableObject = (PoolableObject) gameObjectToPool.copy();
        this.size = size;
        this.availableObjectsPool = new LinkedList<>();
    }

    public static ObjectPool createInstance(PoolableObject gameObjectToPool, int size){
        ObjectPool objectPool = new ObjectPool(gameObjectToPool, size);
        objectPool.gameObject = Game.getScene().createGameObject(gameObjectToPool.name + " pool");
        objectPool.createObjects();

        return objectPool;
    }

    private void createObjects(){
        for(int i = 0; i < size; i++)
            createObject();
    }

    private void createObject(){
        PoolableObject newPoolObject = (PoolableObject) poolableObject.copy();
        //newPoolObject.parent = this;
        newPoolObject.disable();
    }

    public PoolableObject getObject(){
        if (availableObjectsPool.peek() == null)
            createObject();
        PoolableObject newPoolObject = availableObjectsPool.poll();
        assert newPoolObject != null;
        newPoolObject.enable();
        Game.getScene().addGameObjectToScene(newPoolObject);


        return newPoolObject;
    }

    public void returnObjectToPool(PoolableObject poolableObject){
        availableObjectsPool.add(poolableObject);
    }
}
