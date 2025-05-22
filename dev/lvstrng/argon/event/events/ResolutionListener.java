package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.Event;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;
import net.minecraft.class_1041;

public interface ResolutionListener
extends Listener {
    public void onResolution(ResolutionEvent var1);

    public static class ResolutionEvent
    extends Event<ResolutionListener> {
        public class_1041 window;

        public ResolutionEvent(class_1041 window) {
            this.window = window;
        }

        @Override
        public void fire(ArrayList<ResolutionListener> listeners) {
            listeners.forEach(l -> l.onResolution(this));
        }

        @Override
        public Class<ResolutionListener> getListenerType() {
            return ResolutionListener.class;
        }
    }
}
