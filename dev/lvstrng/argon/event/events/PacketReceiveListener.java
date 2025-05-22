package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;
import net.minecraft.class_2596;

public interface PacketReceiveListener
extends Listener {
    public void onPacketReceive(PacketReceiveEvent var1);

    public static class PacketReceiveEvent
    extends CancellableEvent<PacketReceiveListener> {
        public class_2596 packet;

        public PacketReceiveEvent(class_2596 packet) {
            this.packet = packet;
        }

        @Override
        public void fire(ArrayList<PacketReceiveListener> listeners) {
            listeners.forEach(e -> e.onPacketReceive(this));
        }

        @Override
        public Class<PacketReceiveListener> getListenerType() {
            return PacketReceiveListener.class;
        }
    }
}
