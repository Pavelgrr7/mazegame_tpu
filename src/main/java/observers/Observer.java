package observers;

import back.GameObject;
import observers.events.Event;
import observers.events.EventType;

public interface Observer {
    void onNotify(Event event);
//    void onNotify(MenuObject object, Event event);
}
