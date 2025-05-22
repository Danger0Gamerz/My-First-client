package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.AttackListener;
import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.InventoryUtils;
import dev.lvstrng.argon.utils.MouseSimulation;
import dev.lvstrng.argon.utils.WorldUtils;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1802;
import net.minecraft.class_239;
import net.minecraft.class_3966;
import org.lwjgl.glfw.GLFW;

public final class ShieldDisabler
extends Module
implements TickListener,
AttackListener {
    private final NumberSetting hitDelay = new NumberSetting(EncryptedString.of("Hit Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting switchDelay = new NumberSetting(EncryptedString.of("Switch Delay"), 0.0, 20.0, 0.0, 1.0);
    private final BooleanSetting switchBack = new BooleanSetting(EncryptedString.of("Switch Back"), true);
    private final BooleanSetting stun = new BooleanSetting(EncryptedString.of("Stun"), false);
    private final BooleanSetting clickSimulate = new BooleanSetting(EncryptedString.of("Click Simulation"), false);
    private final BooleanSetting requireHoldAxe = new BooleanSetting(EncryptedString.of("Hold Axe"), false);
    int previousSlot;
    int hitClock;
    int switchClock;

    public ShieldDisabler() {
        super(EncryptedString.of("Shield Disabler"), EncryptedString.of("Automatically disables your opponents shield"), -1, Category.COMBAT);
        this.addSettings(this.switchDelay, this.hitDelay, this.switchBack, this.stun, this.clickSimulate, this.requireHoldAxe);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.eventManager.add(AttackListener.class, this);
        this.hitClock = this.hitDelay.getValueInt();
        this.switchClock = this.switchDelay.getValueInt();
        this.previousSlot = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        this.eventManager.remove(AttackListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.mc.field_1755 != null) {
            return;
        }
        if (this.requireHoldAxe.getValue() && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1743)) {
            return;
        }
        class_239 class_2392 = this.mc.field_1765;
        if (class_2392 instanceof class_3966) {
            class_3966 entityHit = (class_3966)class_2392;
            class_1297 entity = entityHit.method_17782();
            if (this.mc.field_1724.method_6115()) {
                return;
            }
            if (entity instanceof class_1657) {
                class_1657 player = (class_1657)entity;
                if (WorldUtils.isShieldFacingAway(player)) {
                    return;
                }
                if (player.method_24518(class_1802.field_8255) && player.method_6039()) {
                    if (this.switchClock > 0) {
                        if (this.previousSlot == -1) {
                            this.previousSlot = this.mc.field_1724.method_31548().field_7545;
                        }
                        --this.switchClock;
                        return;
                    }
                    if (InventoryUtils.selectAxe()) {
                        if (this.hitClock > 0) {
                            --this.hitClock;
                        } else {
                            if (this.clickSimulate.getValue()) {
                                MouseSimulation.mouseClick(0);
                            }
                            WorldUtils.hitEntity((class_1297)player, true);
                            if (this.stun.getValue()) {
                                if (this.clickSimulate.getValue()) {
                                    MouseSimulation.mouseClick(0);
                                }
                                WorldUtils.hitEntity((class_1297)player, true);
                            }
                            this.hitClock = this.hitDelay.getValueInt();
                            this.switchClock = this.switchDelay.getValueInt();
                        }
                    }
                } else if (this.previousSlot != -1) {
                    if (this.switchBack.getValue()) {
                        InventoryUtils.setInvSlot(this.previousSlot);
                    }
                    this.previousSlot = -1;
                }
            }
        }
    }

    @Override
    public void onAttack(AttackListener.AttackEvent event) {
        if (GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)0) != 1) {
            event.cancel();
        }
    }
}
