package dev.lvstrng.argon.event;

import dev.lvstrng.argon.event.Event;
import dev.lvstrng.argon.event.Listener;

public abstract class CancellableEvent<T extends Listener>
extends Event<T> {
    private boolean isCancelled = false;

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }
}
