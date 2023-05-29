package no.brunostengine.components;

import no.brunostengine.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

/**
 * The Component class is the most essential part of a {@link GameObject}.
 * This class fulfills the design principle of Entity-Component Design.
 * TODO: check if correct
 */
public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;

    public void onStart() {

    }

    public void onUpdate(float dt) {

    }

    public void onCollisionEnter(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void endCollision(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void preSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void postSolve(GameObject collidingObject, Contact contact, Vector2f hitNormal) {

    }

    public void generateId() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType) {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntegerValue : enumType.getEnumConstants()) {
            enumValues[i] = enumIntegerValue.name();
            i++;
        }
        return enumValues;
    }

    private int indexOf(String str, String[] arr) {
        for (int i=0; i < arr.length; i++) {
            if (str.equals(arr[i])) {
                return i;
            }
        }

        return -1;
    }

    public void destroy() {

    }

    public int getUid() {
        return this.uid;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }
}
