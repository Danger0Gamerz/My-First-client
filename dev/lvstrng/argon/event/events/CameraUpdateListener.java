package dev.lvstrng.argon.event.events;

import dev.lvstrng.argon.event.CancellableEvent;
import dev.lvstrng.argon.event.Listener;
import java.util.ArrayList;

public interface CameraUpdateListener
extends Listener {
    public void onCameraUpdate(CameraUpdateEvent var1);

    public static class CameraUpdateEvent
    extends CancellableEvent<CameraUpdateListener> {
        public double x;
        public double y;
        public double z;

        public CameraUpdateEvent(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setZ(double z) {
            this.z = z;
        }

        @Override
        public void fire(ArrayList<CameraUpdateListener> listeners) {
            listeners.forEach(l -> l.onCameraUpdate(this));
        }

        @Override
        public Class<CameraUpdateListener> getListenerType() {
            return CameraUpdateListener.class;
        }
    }
}
