package no.brunostengine;

import no.brunostengine.components.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Experimental class. Meant to implement the design principle of Object Pooling as a means of optimizing instantiation
 * in situations where they need to be created regularly and are short-lived. Not fully implemented yet due to time constraint,
 * but instead of removing it for the production branch, we decided to show off the foundation of the work so far as
 * documentation of proof.
 * <br> <a href="https://www.youtube.com/watch?v=fsDE_mO4RZM">Based off of this tutorial</a>
 * <br><b>Note: This class is package-private and will NOT be visible to the client.</b>
 *
 */
class ObjectPool extends Component {
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
