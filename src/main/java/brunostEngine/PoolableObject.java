package brunostEngine;

import components.Component;

public class PoolableObject extends GameObject {
    public ObjectPool objectPoolParent;


    public PoolableObject(String name) {
        super(name);
    }
}
