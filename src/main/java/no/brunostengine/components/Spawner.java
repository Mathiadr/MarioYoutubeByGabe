package no.brunostengine.components;

import no.brunostengine.Game;
import no.brunostengine.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends Component{
    private GameObject spawnObject;
    private List<GameObject> instances;
    private float spawnInterval = 2.0f;
    private float spawnIntervalTimer;
    private int maxInstancesToSpawn = 1;
    private int totalInstancesSpawned = 0;
    private boolean isInfinite = true;

    @Override
    public void onStart() {
        if (spawnObject == null) throw new IllegalArgumentException("Spawner has no assigned GameObject to spawn!");
        spawnObject.transform.position = gameObject.transform.position;
        instances = new ArrayList<>();
        spawnIntervalTimer = spawnInterval;
    }

    @Override
    public void onUpdate(float dt) {
        spawnIntervalTimer -= dt;
        if (spawnIntervalTimer <= 0 && getCurrentInstanceAmount() < maxInstancesToSpawn || isInfinite()){
            GameObject newObj = spawnObject.copy();
            instances.add(newObj);
            totalInstancesSpawned++;
            Game.getScene().addGameObjectToScene(newObj);

            spawnIntervalTimer = spawnInterval;
        }
    }

    public GameObject getSpawnObject() {
        return spawnObject;
    }

    public void setSpawnObject(GameObject spawnObject) {
        this.spawnObject = spawnObject.copy();
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public void setSpawnInterval(float spawnInterval) {
        this.spawnInterval = spawnInterval;
    }

    public int getMaxInstancesToSpawn() {
        return maxInstancesToSpawn;
    }

    public void setMaxInstancesToSpawn(int maxInstancesToSpawn) {
        this.maxInstancesToSpawn = maxInstancesToSpawn;
    }

    public boolean isInfinite() {
        return isInfinite;
    }

    public void setInfinite(boolean infinite) {
        isInfinite = infinite;
    }

    public int getCurrentInstanceAmount(){
        return instances.size();
    }

    public int getTotalInstanceAmount(){
        return totalInstancesSpawned;
    }
}
