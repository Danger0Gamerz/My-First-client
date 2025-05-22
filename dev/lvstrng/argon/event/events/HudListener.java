package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.Event;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;
import net.minecraft.class_332;

public interface HudListener
extends Listener {
    public void onRenderHud(HudEvent var1);

    public static class HudEvent
    extends Event<HudListener> {
        public class_332 context;
        public float delta;

        public HudEvent(class_332 context, float delta) {
            this.context = context;
            this.delta = delta;
        }

        @Override
        public void fire(ArrayList<HudListener> listeners) {
            listeners.forEach(e -> e.onRenderHud(this));
        }

        @Override
        public Class<HudListener> getListenerType() {
            return HudListener.class;
        }
    }
}
