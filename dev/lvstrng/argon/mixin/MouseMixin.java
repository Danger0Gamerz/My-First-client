package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.event.events.ButtonListener;
import dev.lvstrng.argon.event.events.MouseMoveListener;
import dev.lvstrng.argon.event.events.MouseUpdateListener;
import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_312.class})
public class MouseMixin {
    @Shadow
    @Final
    private class_310 field_1779;

    @Inject(method={"updateMouse"}, at={@At(value="RETURN")})
    private void onMouseUpdate(CallbackInfo ci) {
        EventManager.fire(new MouseUpdateListener.MouseUpdateEvent());
    }

    @Inject(method={"onCursorPos"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseMove(long window, double x, double y, CallbackInfo ci) {
        MouseMoveListener.MouseMoveEvent event = new MouseMoveListener.MouseMoveEvent(window, x, y);
        EventManager.fire(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")})
    private void onMousePress(long window, int button, int action, int mods, CallbackInfo ci) {
        EventManager.fire(new ButtonListener.ButtonEvent(button, window, action));
    }
}
