package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.event.events.AttackListener;
import dev.lvstrng.argon.event.events.BlockBreakingListener;
import dev.lvstrng.argon.event.events.ItemUseListener;
import dev.lvstrng.argon.event.events.ResolutionListener;
import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.utils.MouseSimulation;
import net.minecraft.class_1041;
import net.minecraft.class_310;
import net.minecraft.class_638;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_310.class})
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public class_638 field_1687;
    @Shadow
    @Final
    private class_1041 field_1704;

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void onTick(CallbackInfo ci) {
        if (this.field_1687 != null) {
            TickListener.TickEvent event = new TickListener.TickEvent();
            EventManager.fire(event);
        }
    }

    @Inject(method={"onResolutionChanged"}, at={@At(value="HEAD")})
    private void onResolutionChanged(CallbackInfo ci) {
        EventManager.fire(new ResolutionListener.ResolutionEvent(this.field_1704));
    }

    @Inject(method={"doItemUse"}, at={@At(value="HEAD")}, cancellable=true)
    private void onItemUse(CallbackInfo ci) {
        ItemUseListener.ItemUseEvent event = new ItemUseListener.ItemUseEvent();
        EventManager.fire(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
        if (MouseSimulation.isMouseButtonPressed(1)) {
            MouseSimulation.mouseButtons.put(1, false);
            ci.cancel();
        }
    }

    @Inject(method={"doAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAttack(CallbackInfoReturnable<Boolean> cir) {
        AttackListener.AttackEvent event = new AttackListener.AttackEvent();
        EventManager.fire(event);
        if (event.isCancelled()) {
            cir.setReturnValue((Object)false);
        }
        if (MouseSimulation.isMouseButtonPressed(0)) {
            MouseSimulation.mouseButtons.put(0, false);
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"handleBlockBreaking"}, at={@At(value="HEAD")}, cancellable=true)
    private void onBlockBreaking(boolean breaking, CallbackInfo ci) {
        BlockBreakingListener.BlockBreakingEvent event = new BlockBreakingListener.BlockBreakingEvent();
        EventManager.fire(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
        if (MouseSimulation.isMouseButtonPressed(0)) {
            MouseSimulation.mouseButtons.put(0, false);
            ci.cancel();
        }
    }

    @Inject(method={"stop"}, at={@At(value="HEAD")})
    private void onClose(CallbackInfo ci) {
        Argon.INSTANCE.getProfileManager().saveProfile();
    }
}
