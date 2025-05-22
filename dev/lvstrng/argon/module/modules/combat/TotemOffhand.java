package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.InventoryUtils;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2846;

public final class TotemOffhand
extends Module
implements TickListener {
    private final NumberSetting switchDelay = new NumberSetting(EncryptedString.of("Switch Delay"), 0.0, 5.0, 0.0, 1.0);
    private final NumberSetting equipDelay = new NumberSetting(EncryptedString.of("Equip Delay"), 1.0, 5.0, 1.0, 1.0);
    private final BooleanSetting switchBack = new BooleanSetting(EncryptedString.of("Switch Back"), false);
    private int switchClock;
    private int equipClock;
    private int switchBackClock;
    private int previousSlot = -1;
    boolean sent;
    boolean active = false;

    public TotemOffhand() {
        super(EncryptedString.of("Totem Offhand"), EncryptedString.of("Switches to your totem slot and offhands a totem if you dont have one already"), -1, Category.COMBAT);
        this.addSettings(this.switchDelay, this.equipDelay, this.switchBack);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.mc.field_1755 != null) {
            return;
        }
        if (this.mc.field_1724.method_6079().method_7909() != class_1802.field_8288) {
            this.active = true;
        }
        if (this.active) {
            if (this.switchClock < this.switchDelay.getValueInt()) {
                ++this.switchClock;
                return;
            }
            if (this.previousSlot == -1) {
                this.previousSlot = this.mc.field_1724.method_31548().field_7545;
            }
            if (InventoryUtils.selectItemFromHotbar(class_1802.field_8288)) {
                if (this.equipClock < this.equipDelay.getValueInt()) {
                    ++this.equipClock;
                    return;
                }
                if (!this.sent) {
                    this.mc.method_1562().method_48296().method_10743((class_2596)new class_2846(class_2846.class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
                    this.sent = true;
                    return;
                }
            }
            if ((double)this.switchBackClock < this.switchDelay.getValue()) {
                ++this.switchBackClock;
            } else {
                if (this.switchBack.getValue()) {
                    InventoryUtils.setInvSlot(this.previousSlot);
                }
                this.reset();
            }
        }
    }

    public void reset() {
        this.switchClock = 0;
        this.equipClock = 0;
        this.switchBackClock = 0;
        this.previousSlot = -1;
        this.sent = false;
        this.active = false;
    }
}
