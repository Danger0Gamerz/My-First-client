package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;

public interface ItemUseListener
extends Listener {
    public void onItemUse(ItemUseEvent var1);

    public static class ItemUseEvent
    extends CancellableEvent<ItemUseListener> {
        @Override
        public void fire(ArrayList<ItemUseListener> listeners) {
            listeners.forEach(e -> e.onItemUse(this));
        }

        @Override
        public Class<ItemUseListener> getListenerType() {
            return ItemUseListener.class;
        }
    }
}
