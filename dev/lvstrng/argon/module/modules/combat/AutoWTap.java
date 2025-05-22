package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.HudListener;
import dev.lvstrng.argon.event.events.PacketSendListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.TimerUtils;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import org.lwjgl.glfw.GLFW;

public final class AutoWTap
extends Module
implements PacketSendListener,
HudListener {
    private final MinMaxSetting delay = new MinMaxSetting(EncryptedString.of("Delay"), 0.0, 1000.0, 1.0, 230.0, 270.0);
    private final BooleanSetting inAir = (BooleanSetting)new BooleanSetting(EncryptedString.of("In Air"), false).setDescription(EncryptedString.of("Whether it should W tap in air"));
    private final BooleanSetting targetSelection = (BooleanSetting)new BooleanSetting(EncryptedString.of("Target Selection"), false).setDescription(EncryptedString.of("Optimizes tapping based on target distance"));
    private final BooleanSetting allowJumpStops = (BooleanSetting)new BooleanSetting(EncryptedString.of("Allow Jump Stops"), false).setDescription(EncryptedString.of("Allows w-taps to continue after a player is hit, even if jumping afterward"));
    private final TimerUtils sprintTimer = new TimerUtils();
    private final TimerUtils tapTimer = new TimerUtils();
    private boolean holdingForward;
    private boolean sprinting;
    private int currentDelay;
    private boolean jumpedWhileHitting;
    private class_1297 targetEntity;
    private boolean shouldSTap;
    private boolean isSTapping;
    private boolean wasHoldingForward;
    private boolean completedSTap = false;
    private boolean wasMovingForward;
    private boolean wasInAir = false;
    private boolean wtapCancelled = false;

    public AutoWTap() {
        super(EncryptedString.of("Auto WTap"), EncryptedString.of("Automatically W Taps for you so the opponent takes more knockback"), -1, Category.COMBAT);
        this.addSettings(this.delay, this.inAir, this.targetSelection, this.allowJumpStops);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(PacketSendListener.class, this);
        this.eventManager.add(HudListener.class, this);
        this.currentDelay = this.delay.getRandomValueInt();
        this.jumpedWhileHitting = false;
        this.wtapCancelled = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(PacketSendListener.class, this);
        this.eventManager.remove(HudListener.class, this);
        if (this.isSTapping) {
            this.mc.field_1690.field_1881.method_23481(false);
            this.isSTapping = false;
        }
        if (this.wasHoldingForward) {
            this.mc.field_1690.field_1894.method_23481(true);
            this.wasHoldingForward = false;
        }
        this.wasMovingForward = false;
        this.completedSTap = false;
        this.wasInAir = false;
        this.wtapCancelled = false;
        super.onDisable();
    }

    @Override
    public void onRenderHud(HudListener.HudEvent event) {
        if (!this.targetSelection.getValue()) {
            this.handleNormalWTap();
            return;
        }
        if (GLFW.glfwGetKey((long)this.mc.method_22683().method_4490(), (int)87) != 1) {
            this.resetMovementState();
            this.wasMovingForward = false;
            this.completedSTap = false;
            this.targetEntity = null;
            return;
        }
        if (this.isSTapping && GLFW.glfwGetKey((long)this.mc.method_22683().method_4490(), (int)32) == 1 || this.wasInAir && this.mc.field_1724.method_24828()) {
            this.resetMovementState();
            this.completedSTap = true;
            this.wasInAir = false;
            return;
        }
        if (!this.mc.field_1724.method_24828()) {
            this.wasInAir = true;
        }
        if (this.jumpedWhileHitting && this.inAir.getValue() && this.targetSelection.getValue()) {
            if (!this.mc.field_1724.method_24828()) {
                this.mc.field_1690.field_1881.method_23481(true);
                this.mc.field_1690.field_1894.method_23481(false);
                this.isSTapping = true;
                return;
            }
            this.jumpedWhileHitting = false;
            this.resetMovementState();
        }
        if (this.jumpedWhileHitting) {
            if (!this.mc.field_1724.method_24828()) {
                this.resetMovementState();
                return;
            }
            this.jumpedWhileHitting = false;
        }
        if (this.targetEntity == null || !this.wasMovingForward || !this.inAir.getValue() && !this.mc.field_1724.method_24828()) {
            this.resetMovementState();
            return;
        }
        double distance = this.mc.field_1724.method_5858(this.targetEntity);
        if (!this.completedSTap && distance >= 2.25 && distance <= 7.29) {
            if (GLFW.glfwGetKey((long)this.mc.method_22683().method_4490(), (int)87) == 1) {
                this.wasHoldingForward = true;
                this.mc.field_1690.field_1894.method_23481(false);
            }
            this.mc.field_1690.field_1881.method_23481(true);
            this.isSTapping = true;
        } else if (distance > 7.29) {
            if (this.isSTapping) {
                this.resetMovementState();
                this.completedSTap = true;
            }
            if (distance > 9.0) {
                this.targetEntity = null;
                this.completedSTap = false;
            }
        }
    }

    private void handleNormalWTap() {
        if (GLFW.glfwGetKey((long)this.mc.method_22683().method_4490(), (int)87) != 1) {
            this.sprinting = false;
            this.holdingForward = false;
            this.wtapCancelled = false;
            return;
        }
        if (!(this.inAir.getValue() || this.allowJumpStops.getValue() || this.mc.field_1724.method_24828())) {
            return;
        }
        if (GLFW.glfwGetKey((long)this.mc.method_22683().method_4490(), (int)32) == 1 && !this.allowJumpStops.getValue() && (this.holdingForward || this.sprinting)) {
            this.mc.field_1690.field_1894.method_23481(true);
            this.holdingForward = false;
            this.sprinting = false;
            this.wtapCancelled = true;
            return;
        }
        if (this.wtapCancelled) {
            return;
        }
        if (this.holdingForward && this.tapTimer.delay(1.0f)) {
            this.mc.field_1690.field_1894.method_23481(false);
            this.sprintTimer.reset();
            this.sprinting = true;
            this.holdingForward = false;
        }
        if (this.sprinting && this.sprintTimer.delay(this.currentDelay)) {
            this.mc.field_1690.field_1894.method_23481(true);
            this.sprinting = false;
            this.currentDelay = this.delay.getRandomValueInt();
        }
    }

    private void resetMovementState() {
        if (this.isSTapping) {
            this.mc.field_1690.field_1881.method_23481(false);
            this.isSTapping = false;
        }
        if (this.wasHoldingForward) {
            this.mc.field_1690.field_1894.method_23481(true);
            this.wasHoldingForward = false;
        }
    }

    @Override
    public void onPacketSend(PacketSendListener.PacketSendEvent event) {
        class_2596 class_25962 = event.packet;
        if (!(class_25962 instanceof class_2824)) {
            return;
        }
        class_2824 packet = (class_2824)class_25962;
        packet.method_34209(new class_2824.class_5908(){

            public void method_34219(class_1268 hand) {
            }

            public void method_34220(class_1268 hand, class_243 pos) {
            }

            public void method_34218() {
                if (!(AutoWTap.this.allowJumpStops.getValue() || AutoWTap.this.inAir.getValue() || ((AutoWTap)AutoWTap.this).mc.field_1724.method_24828())) {
                    return;
                }
                if (AutoWTap.this.targetSelection.getValue()) {
                    boolean bl = AutoWTap.this.wasMovingForward = GLFW.glfwGetKey((long)AutoWTap.this.mc.method_22683().method_4490(), (int)87) == 1 && ((AutoWTap)AutoWTap.this).mc.field_1724.method_5624();
                    if (AutoWTap.this.wasMovingForward) {
                        AutoWTap.this.targetEntity = ((AutoWTap)AutoWTap.this).mc.field_1692;
                        AutoWTap.this.completedSTap = false;
                        if (GLFW.glfwGetKey((long)AutoWTap.this.mc.method_22683().method_4490(), (int)87) == 1) {
                            AutoWTap.this.wasHoldingForward = true;
                            ((AutoWTap)AutoWTap.this).mc.field_1690.field_1894.method_23481(false);
                        }
                    }
                } else if (AutoWTap.this.inAir.getValue() && AutoWTap.this.targetSelection.getValue() && !((AutoWTap)AutoWTap.this).mc.field_1724.method_24828()) {
                    AutoWTap.this.jumpedWhileHitting = true;
                } else {
                    if (GLFW.glfwGetKey((long)AutoWTap.this.mc.method_22683().method_4490(), (int)32) == 1) {
                        AutoWTap.this.wtapCancelled = true;
                        return;
                    }
                    if (((AutoWTap)AutoWTap.this).mc.field_1690.field_1894.method_1434() && ((AutoWTap)AutoWTap.this).mc.field_1724.method_5624()) {
                        AutoWTap.this.sprintTimer.reset();
                        AutoWTap.this.holdingForward = true;
                        AutoWTap.this.wtapCancelled = false;
                    }
                }
                AutoWTap.this.holdingForward = true;
                AutoWTap.this.wtapCancelled = false;
            }
        });
    }
}
