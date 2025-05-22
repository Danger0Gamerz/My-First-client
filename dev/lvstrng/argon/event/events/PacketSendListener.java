package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;
import net.minecraft.class_2596;

public interface PacketSendListener
extends Listener {
    public void onPacketSend(PacketSendEvent var1);

    public static class PacketSendEvent
    extends CancellableEvent<PacketSendListener> {
        public class_2596 packet;

        public PacketSendEvent(class_2596 packet) {
            this.packet = packet;
        }

        @Override
        public void fire(ArrayList<PacketSendListener> listeners) {
            listeners.forEach(e -> e.onPacketSend(this));
        }

        @Override
        public Class<PacketSendListener> getListenerType() {
            return PacketSendListener.class;
        }
    }
}
