package observers;

import back.GameObject;
import back.MenuObject;
import observers.events.Event;
import observers.events.EventType;

public interface Observer {
    void onNotify(Event event);
//    void onNotify(MenuObject object, Event event);
}
