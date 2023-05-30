package no.brunostengine;

/**
 * Part of the experimental and unfinished implementation of object pooling system. See {@link ObjectPool} for more info.
 */
public class PoolableObject extends GameObject {
    public ObjectPool objectPoolParent;


    public PoolableObject(String name) {
        super(name);
    }
}
