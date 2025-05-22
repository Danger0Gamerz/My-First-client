package dev.lvstrng.argon.module.modules.misc;

import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.KeybindSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.InventoryUtils;
import dev.lvstrng.argon.utils.KeyUtils;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1802;

public final class KeyPearl
extends Module
implements TickListener {
    private final KeybindSetting activateKey = new KeybindSetting(EncryptedString.of("Activate Key"), -1, false);
    private final NumberSetting delay = new NumberSetting(EncryptedString.of("Delay"), 0.0, 20.0, 0.0, 1.0);
    private final BooleanSetting switchBack = new BooleanSetting(EncryptedString.of("Switch Back"), true);
    private final NumberSetting switchDelay = (NumberSetting)new NumberSetting(EncryptedString.of("Switch Delay"), 0.0, 20.0, 0.0, 1.0).setDescription(EncryptedString.of("Delay after throwing pearl before switching back"));
    private boolean active;
    private boolean hasActivated;
    private int clock;
    private int previousSlot;
    private int switchClock;

    public KeyPearl() {
        super(EncryptedString.of("Key Pearl"), EncryptedString.of("Switches to an ender pearl and throws it when you press a bind"), -1, Category.MISC);
        this.addSettings(this.activateKey, this.delay, this.switchBack, this.switchDelay);
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
        if (KeyUtils.isKeyPressed(this.activateKey.getKey())) {
            this.active = true;
        }
        if (this.active) {
            if (this.previousSlot == -1) {
                this.previousSlot = this.mc.field_1724.method_31548().field_7545;
            }
            InventoryUtils.selectItemFromHotbar(class_1802.field_8634);
            if (this.clock < this.delay.getValueInt()) {
                ++this.clock;
                return;
            }
            if (!this.hasActivated) {
                class_1269 result = this.mc.field_1761.method_2919((class_1657)this.mc.field_1724, class_1268.field_5808);
                if (result.method_23665() && result.method_23666()) {
                    this.mc.field_1724.method_6104(class_1268.field_5808);
                }
                this.hasActivated = true;
            }
            if (this.switchBack.getValue()) {
                this.switchBack();
            } else {
                this.reset();
            }
        }
    }

    private void switchBack() {
        if (this.switchClock < this.switchDelay.getValueInt()) {
            ++this.switchClock;
            return;
        }
        InventoryUtils.setInvSlot(this.previousSlot);
        this.reset();
    }

    private void reset() {
        this.previousSlot = -1;
        this.clock = 0;
        this.switchClock = 0;
        this.active = false;
        this.hasActivated = false;
    }
}
