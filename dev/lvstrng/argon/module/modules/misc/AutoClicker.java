package dev.lvstrng.argon.module.modules.misc;

import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.mixin.MinecraftClientAccessor;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.MouseSimulation;
import dev.lvstrng.argon.utils.TimerUtils;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1811;
import net.minecraft.class_1829;
import net.minecraft.class_239;
import net.minecraft.class_9334;
import org.lwjgl.glfw.GLFW;

public final class AutoClicker
extends Module
implements TickListener {
    private final BooleanSetting onlyWeapon = (BooleanSetting)new BooleanSetting(EncryptedString.of("Only Weapon"), true).setDescription(EncryptedString.of("Only left clicks with weapon in hand"));
    private final BooleanSetting onlyBlocks = (BooleanSetting)new BooleanSetting(EncryptedString.of("Only Blocks"), true).setDescription(EncryptedString.of("Only right clicks blocks"));
    private final BooleanSetting onClick = new BooleanSetting(EncryptedString.of("On Click"), true);
    private final NumberSetting delay = new NumberSetting(EncryptedString.of("Delay"), 0.0, 1000.0, 0.0, 1.0);
    private final NumberSetting chance = new NumberSetting(EncryptedString.of("Chance"), 0.0, 100.0, 100.0, 1.0);
    private final ModeSetting<Mode> mode = new ModeSetting<Mode>(EncryptedString.of("Actions"), Mode.All, Mode.class);
    private final TimerUtils timer = new TimerUtils();

    public AutoClicker() {
        super(EncryptedString.of("Auto Clicker"), EncryptedString.of("Automatically clicks for you"), -1, Category.MISC);
        this.addSettings(this.onlyWeapon, this.onClick, this.delay, this.chance, this.mode);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.mc.field_1724 == null) {
            return;
        }
        if (this.mc.field_1755 != null) {
            return;
        }
        if (this.mc.field_1765 == null) {
            return;
        }
        if (this.timer.delay(this.delay.getValueFloat()) && this.chance.getValueInt() >= MathUtils.randomInt(1, 100)) {
            if (this.mode.isMode(Mode.Left)) {
                this.performLeftClick();
            }
            if (this.mode.isMode(Mode.Right)) {
                this.performRightClick();
            }
            if (this.mode.isMode(Mode.All)) {
                this.performLeftClick();
                this.performRightClick();
            }
        }
    }

    private void performRightClick() {
        class_1792 mainhand = this.mc.field_1724.method_6047().method_7909();
        class_1792 offhand = this.mc.field_1724.method_6079().method_7909();
        if (mainhand.method_57347().method_57832(class_9334.field_50075)) {
            return;
        }
        if (offhand.method_57347().method_57832(class_9334.field_50075)) {
            return;
        }
        if (mainhand instanceof class_1811 || offhand instanceof class_1811) {
            return;
        }
        if (this.onClick.getValue() && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)1) != 1) {
            return;
        }
        MouseSimulation.mouseClick(1);
        ((MinecraftClientAccessor)this.mc).invokeDoItemUse();
        this.timer.reset();
    }

    private void performLeftClick() {
        class_1792 mainhand = this.mc.field_1724.method_6047().method_7909();
        class_1792 offhand = this.mc.field_1724.method_6079().method_7909();
        if (this.mc.field_1765.method_17783() == class_239.class_240.field_1332) {
            return;
        }
        if (this.mc.field_1724.method_6115()) {
            return;
        }
        if (this.onlyWeapon.getValue() && !(mainhand instanceof class_1829) && !(mainhand instanceof class_1743)) {
            return;
        }
        if (this.onClick.getValue() && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)0) != 1) {
            return;
        }
        MouseSimulation.mouseClick(0);
        ((MinecraftClientAccessor)this.mc).invokeDoAttack();
        this.timer.reset();
    }

    public static enum Mode {
        All,
        Left,
        Right;

    }
}
