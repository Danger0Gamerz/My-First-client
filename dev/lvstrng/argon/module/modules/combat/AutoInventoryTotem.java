package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.FakeInvScreen;
import dev.lvstrng.argon.utils.InventoryUtils;
import dev.lvstrng.argon.utils.TimerUtils;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_437;
import net.minecraft.class_490;

public final class AutoInventoryTotem
extends Module
implements TickListener {
    private final ModeSetting<Mode> mode = (ModeSetting)new ModeSetting<Mode>(EncryptedString.of("Mode"), Mode.Blatant, Mode.class).setDescription(EncryptedString.of("Whether to randomize the toteming pattern or no"));
    private final NumberSetting delay = new NumberSetting(EncryptedString.of("Delay"), 0.0, 20.0, 0.0, 1.0);
    private final BooleanSetting hotbar = (BooleanSetting)new BooleanSetting(EncryptedString.of("Hotbar"), true).setDescription(EncryptedString.of("Puts a totem in your hotbar as well, if enabled (Setting below will work if this is enabled)"));
    private final NumberSetting totemSlot = (NumberSetting)new NumberSetting(EncryptedString.of("Totem Slot"), 1.0, 9.0, 1.0, 1.0).setDescription(EncryptedString.of("Your preferred totem slot"));
    private final BooleanSetting autoSwitch = (BooleanSetting)new BooleanSetting(EncryptedString.of("Auto Switch"), false).setDescription(EncryptedString.of("Switches to totem slot when going inside the inventory"));
    private final BooleanSetting forceTotem = (BooleanSetting)new BooleanSetting(EncryptedString.of("Force Totem"), false).setDescription(EncryptedString.of("Puts the totem in the slot, regardless if its space is taken up by something else"));
    private final BooleanSetting autoOpen = (BooleanSetting)new BooleanSetting(EncryptedString.of("Auto Open"), false).setDescription(EncryptedString.of("Automatically opens and closes the inventory for you"));
    private final NumberSetting stayOpenFor = new NumberSetting(EncryptedString.of("Stay Open For"), 0.0, 20.0, 0.0, 1.0);
    int clock = -1;
    int closeClock = -1;
    TimerUtils openTimer = new TimerUtils();
    TimerUtils closeTimer = new TimerUtils();

    public AutoInventoryTotem() {
        super(EncryptedString.of("Auto Inventory Totem"), EncryptedString.of("Automatically equips a totem in your offhand and main hand if empty"), -1, Category.COMBAT);
        this.addSettings(this.mode, this.delay, this.hotbar, this.totemSlot, this.autoSwitch, this.forceTotem, this.autoOpen, this.stayOpenFor);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.clock = -1;
        this.closeClock = -1;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.shouldOpenScreen() && this.autoOpen.getValue()) {
            this.mc.method_1507((class_437)new FakeInvScreen((class_1657)this.mc.field_1724));
        }
        if (!(this.mc.field_1755 instanceof class_490) && !(this.mc.field_1755 instanceof FakeInvScreen)) {
            this.clock = -1;
            this.closeClock = -1;
            return;
        }
        if (this.clock == -1) {
            this.clock = this.delay.getValueInt();
        }
        if (this.closeClock == -1) {
            this.closeClock = this.stayOpenFor.getValueInt();
        }
        if (this.clock > 0) {
            --this.clock;
        }
        class_1661 inventory = this.mc.field_1724.method_31548();
        if (this.autoSwitch.getValue()) {
            inventory.field_7545 = this.totemSlot.getValueInt() - 1;
        }
        if (this.clock <= 0) {
            class_1799 mainHand;
            if (((class_1799)inventory.field_7544.get(0)).method_7909() != class_1802.field_8288) {
                int slot;
                int n = slot = this.mode.isMode(Mode.Blatant) ? InventoryUtils.findTotemSlot() : InventoryUtils.findRandomTotemSlot();
                if (slot != -1) {
                    this.mc.field_1761.method_2906(((class_1723)((class_490)this.mc.field_1755).method_17577()).field_7763, slot, 40, class_1713.field_7791, (class_1657)this.mc.field_1724);
                    return;
                }
            }
            if (this.hotbar.getValue() && ((mainHand = this.mc.field_1724.method_6047()).method_7960() || this.forceTotem.getValue() && mainHand.method_7909() != class_1802.field_8288)) {
                int slot;
                int n = slot = this.mode.isMode(Mode.Blatant) ? InventoryUtils.findTotemSlot() : InventoryUtils.findRandomTotemSlot();
                if (slot != -1) {
                    this.mc.field_1761.method_2906(((class_1723)((class_490)this.mc.field_1755).method_17577()).field_7763, slot, inventory.field_7545, class_1713.field_7791, (class_1657)this.mc.field_1724);
                    return;
                }
            }
            if (this.shouldCloseScreen() && this.autoOpen.getValue()) {
                if (this.closeClock != 0) {
                    --this.closeClock;
                    return;
                }
                this.mc.field_1755.method_25419();
                this.closeClock = this.stayOpenFor.getValueInt();
            }
        }
    }

    public boolean shouldCloseScreen() {
        if (this.hotbar.getValue()) {
            return this.mc.field_1724.method_31548().method_5438(this.totemSlot.getValueInt() - 1).method_7909() == class_1802.field_8288 && this.mc.field_1724.method_6079().method_7909() == class_1802.field_8288 && this.mc.field_1755 instanceof FakeInvScreen;
        }
        return this.mc.field_1724.method_6079().method_7909() == class_1802.field_8288 && this.mc.field_1755 instanceof FakeInvScreen;
    }

    public boolean shouldOpenScreen() {
        if (this.hotbar.getValue()) {
            return (this.mc.field_1724.method_6079().method_7909() != class_1802.field_8288 || this.mc.field_1724.method_31548().method_5438(this.totemSlot.getValueInt() - 1).method_7909() != class_1802.field_8288) && !(this.mc.field_1755 instanceof FakeInvScreen) && InventoryUtils.countItemExceptHotbar(item -> item == class_1802.field_8288) != 0;
        }
        return this.mc.field_1724.method_6079().method_7909() != class_1802.field_8288 && !(this.mc.field_1755 instanceof FakeInvScreen) && InventoryUtils.countItemExceptHotbar(item -> item == class_1802.field_8288) != 0;
    }

    public static enum Mode {
        Blatant,
        Random;

    }
}
