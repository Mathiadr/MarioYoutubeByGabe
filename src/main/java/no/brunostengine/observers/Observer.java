package no.brunostengine.observers;

import no.brunostengine.GameObject;
import no.brunostengine.observers.events.Event;

public interface Observer {
    void onNotify(GameObject object, Event event);
}
