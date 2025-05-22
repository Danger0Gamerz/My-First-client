package dev.lvstrng.argon.module.modules.misc;

import dev.lvstrng.argon.event.events.ItemUseListener;
import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.MouseSimulation;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import org.lwjgl.glfw.GLFW;

public final class AutoXP
extends Module
implements TickListener,
ItemUseListener {
    private final NumberSetting delay = new NumberSetting(EncryptedString.of("Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting chance = (NumberSetting)new NumberSetting(EncryptedString.of("Chance"), 0.0, 100.0, 100.0, 1.0).setDescription(EncryptedString.of("Randomization"));
    private final BooleanSetting clickSimulation = (BooleanSetting)new BooleanSetting(EncryptedString.of("Click Simulation"), false).setDescription(EncryptedString.of("Makes the CPS hud think you're legit"));
    int clock;

    public AutoXP() {
        super(EncryptedString.of("Auto XP"), EncryptedString.of("Automatically throws XP bottles for you"), -1, Category.MISC);
        this.addSettings(this.delay, this.chance, this.clickSimulation);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.eventManager.add(ItemUseListener.class, this);
        this.clock = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        this.eventManager.remove(ItemUseListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.mc.field_1755 != null) {
            return;
        }
        boolean dontThrow = this.clock != 0;
        int randomInt = MathUtils.randomInt(1, 100);
        if (this.mc.field_1724.method_6047().method_7909() != class_1802.field_8287) {
            return;
        }
        if (GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)1) != 1) {
            return;
        }
        if (dontThrow) {
            --this.clock;
        }
        if (!dontThrow && randomInt <= this.chance.getValueInt()) {
            class_1269 result;
            if (this.clickSimulation.getValue()) {
                MouseSimulation.mouseClick(1);
            }
            if ((result = this.mc.field_1761.method_2919((class_1657)this.mc.field_1724, class_1268.field_5808)).method_23665() && result.method_23666()) {
                this.mc.field_1724.method_6104(class_1268.field_5808);
            }
            this.clock = this.delay.getValueInt();
        }
    }

    @Override
    public void onItemUse(ItemUseListener.ItemUseEvent event) {
        if (this.mc.field_1724.method_6047().method_7909() == class_1802.field_8287) {
            event.cancel();
        }
    }
}
