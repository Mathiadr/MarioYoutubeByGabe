package brunostEngine;

import components.Component;

import java.util.HashMap;

public class Bullet extends PoolableObject {
    public float speed;
    public GameObject player;
    public ObjectPool objectPool;

    public Bullet(String name) {
        super(name);
    }

    @Override
    public void onStart() {
        objectPool = ObjectPool.createInstance(this, 4);
    }

    @Override
    public void onUpdate(float dt) {
        super.onUpdate(dt);
    }
}
