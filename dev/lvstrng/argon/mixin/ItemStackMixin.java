package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.modules.render.NoBounce;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1799.class})
public class ItemStackMixin {
    @Inject(method={"getBobbingAnimationTime"}, at={@At(value="HEAD")}, cancellable=true)
    private void removeBounceAnimation(CallbackInfoReturnable<Integer> cir) {
        class_1799 mainHandStack;
        if (Argon.mc.field_1724 == null) {
            return;
        }
        NoBounce noBounce = Argon.INSTANCE.getModuleManager().getModule(NoBounce.class);
        if (Argon.INSTANCE != null && Argon.mc.field_1724 != null && noBounce.isEnabled() && (mainHandStack = Argon.mc.field_1724.method_6047()).method_31574(class_1802.field_8301)) {
            cir.setReturnValue((Object)0);
        }
    }
}
