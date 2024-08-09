package observers;

import back.GameObject;
import observers.events.Event;
import observers.events.EventType;

public interface Observer {
    void onNotify(GameObject object, Event event);
}
