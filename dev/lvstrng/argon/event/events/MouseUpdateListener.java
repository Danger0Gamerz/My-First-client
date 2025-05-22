package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.Event;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;

public interface MouseUpdateListener
extends Listener {
    public void onMouseUpdate();

    public static class MouseUpdateEvent
    extends Event<MouseUpdateListener> {
        @Override
        public void fire(ArrayList<MouseUpdateListener> listeners) {
            listeners.forEach(MouseUpdateListener::onMouseUpdate);
        }

        @Override
        public Class<MouseUpdateListener> getListenerType() {
            return MouseUpdateListener.class;
        }
    }
}
