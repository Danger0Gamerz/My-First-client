package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;

public interface AttackListener
extends Listener {
    public void onAttack(AttackEvent var1);

    public static class AttackEvent
    extends CancellableEvent<AttackListener> {
        @Override
        public void fire(ArrayList<AttackListener> listeners) {
            listeners.forEach(e -> e.onAttack(this));
        }

        @Override
        public Class<AttackListener> getListenerType() {
            return AttackListener.class;
        }
    }
}
