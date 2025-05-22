package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.event.events.GameRenderListener;
import dev.lvstrng.argon.module.modules.misc.Freecam;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_757.class})
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private class_4184 field_18765;

    @Shadow
    public abstract Matrix4f method_22973(double var1);

    @Shadow
    protected abstract double method_3196(class_4184 var1, float var2, boolean var3);

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal=1)})
    private void onWorldRender(class_9779 tickCounter, CallbackInfo ci) {
        double d = this.method_3196(this.field_18765, tickCounter.method_60637(true), true);
        Matrix4f matrix4f = this.method_22973(d);
        class_4587 matrixStack = new class_4587();
        EventManager.fire(new GameRenderListener.GameRenderEvent(matrixStack, tickCounter.method_60637(true)));
    }

    @Inject(method={"shouldRenderBlockOutline"}, at={@At(value="HEAD")}, cancellable=true)
    private void onShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        if (Argon.INSTANCE.getModuleManager().getModule(Freecam.class).isEnabled()) {
            cir.setReturnValue((Object)false);
        }
    }
}
