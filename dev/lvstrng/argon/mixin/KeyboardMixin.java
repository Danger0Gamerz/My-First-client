package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.event.events.ButtonListener;
import net.minecraft.class_309;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_309.class})
public class KeyboardMixin {
    @Shadow
    @Final
    private class_310 field_1678;

    @Inject(method={"onKey"}, at={@At(value="HEAD")})
    private void onPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        EventManager.fire(new ButtonListener.ButtonEvent(key, window, action));
    }
}
