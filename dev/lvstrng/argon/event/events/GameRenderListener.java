package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.Event;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;
import net.minecraft.class_4587;

public interface GameRenderListener
extends Listener {
    public void onGameRender(GameRenderEvent var1);

    public static class GameRenderEvent
    extends Event<GameRenderListener> {
        public class_4587 matrices;
        public float delta;

        public GameRenderEvent(class_4587 matrices, float delta) {
            this.matrices = matrices;
            this.delta = delta;
        }

        @Override
        public void fire(ArrayList<GameRenderListener> listeners) {
            listeners.forEach(e -> e.onGameRender(this));
        }

        @Override
        public Class<GameRenderListener> getListenerType() {
            return GameRenderListener.class;
        }
    }
}
