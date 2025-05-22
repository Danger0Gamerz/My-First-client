package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.mixin.HandledScreenMixin;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1802;
import net.minecraft.class_437;
import net.minecraft.class_490;

public final class HoverTotem
extends Module
implements TickListener {
    private final NumberSetting delay = new NumberSetting(EncryptedString.of("Delay"), 0.0, 20.0, 0.0, 1.0);
    private final BooleanSetting hotbar = (BooleanSetting)new BooleanSetting(EncryptedString.of("Hotbar"), true).setDescription(EncryptedString.of("Puts a totem in your hotbar as well, if enabled (Setting below will work if this is enabled)"));
    private final NumberSetting slot = (NumberSetting)new NumberSetting(EncryptedString.of("Totem Slot"), 1.0, 9.0, 1.0, 1.0).setDescription(EncryptedString.of("Your preferred totem slot"));
    private final BooleanSetting autoSwitch = (BooleanSetting)new BooleanSetting(EncryptedString.of("Auto Switch"), false).setDescription(EncryptedString.of("Switches to totem slot when going inside the inventory"));
    private int clock;

    public HoverTotem() {
        super(EncryptedString.of("Hover Totem"), EncryptedString.of("Equips a totem in your totem and offhand slots if a totem is hovered"), -1, Category.COMBAT);
        this.addSettings(this.delay, this.hotbar, this.slot, this.autoSwitch);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.clock = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        class_437 class_4372 = this.mc.field_1755;
        if (class_4372 instanceof class_490) {
            class_490 inv = (class_490)class_4372;
            class_1735 hoveredSlot = ((HandledScreenMixin)inv).getFocusedSlot();
            if (this.autoSwitch.getValue()) {
                this.mc.field_1724.method_31548().field_7545 = this.slot.getValueInt() - 1;
            }
            if (hoveredSlot != null) {
                int slot = hoveredSlot.method_34266();
                if (slot > 35) {
                    return;
                }
                int totem = this.slot.getValueInt() - 1;
                if (hoveredSlot.method_7677().method_7909() == class_1802.field_8288) {
                    if (this.hotbar.getValue() && this.mc.field_1724.method_31548().method_5438(totem).method_7909() != class_1802.field_8288) {
                        if (this.clock > 0) {
                            --this.clock;
                            return;
                        }
                        this.mc.field_1761.method_2906(((class_1723)inv.method_17577()).field_7763, slot, totem, class_1713.field_7791, (class_1657)this.mc.field_1724);
                        this.clock = this.delay.getValueInt();
                    } else if (!this.mc.field_1724.method_6079().method_31574(class_1802.field_8288)) {
                        if (this.clock > 0) {
                            --this.clock;
                            return;
                        }
                        this.mc.field_1761.method_2906(((class_1723)inv.method_17577()).field_7763, slot, 40, class_1713.field_7791, (class_1657)this.mc.field_1724);
                        this.clock = this.delay.getValueInt();
                    }
                }
            }
        } else {
            this.clock = this.delay.getValueInt();
        }
    }
}
