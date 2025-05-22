package dev.lvstrng.argon.utils;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.mixin.MinecraftClientAccessor;
import dev.lvstrng.argon.mixin.MouseHandlerAccessor;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MouseSimulation {
    public static HashMap<Integer, Boolean> mouseButtons = new HashMap();
    public static ExecutorService clickExecutor = Executors.newFixedThreadPool(100);

    public static MouseHandlerAccessor getMouseHandler() {
        return (MouseHandlerAccessor)((MinecraftClientAccessor)Argon.mc).getMouse();
    }

    public static boolean isMouseButtonPressed(int keyCode) {
        Boolean key = mouseButtons.get(keyCode);
        return key != null ? key : false;
    }

    public static void mousePress(int keyCode) {
        mouseButtons.put(keyCode, true);
        MouseSimulation.getMouseHandler().press(Argon.mc.method_22683().method_4490(), keyCode, 1, 0);
    }

    public static void mouseRelease(int keyCode) {
        MouseSimulation.getMouseHandler().press(Argon.mc.method_22683().method_4490(), keyCode, 0, 0);
    }

    public static void mouseClick(int keyCode, int millis) {
        clickExecutor.submit(() -> {
            try {
                MouseSimulation.mousePress(keyCode);
                Thread.sleep(millis);
                MouseSimulation.mouseRelease(keyCode);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        });
    }

    public static void mouseClick(int keyCode) {
        MouseSimulation.mouseClick(keyCode, 35);
    }
}
