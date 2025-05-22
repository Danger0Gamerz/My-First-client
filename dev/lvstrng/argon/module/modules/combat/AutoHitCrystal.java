package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.AttackListener;
import dev.lvstrng.argon.event.events.ItemUseListener;
import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.combat.AutoCrystal;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.KeybindSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.BlockUtils;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.InventoryUtils;
import dev.lvstrng.argon.utils.KeyUtils;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.MouseSimulation;
import dev.lvstrng.argon.utils.WorldUtils;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1829;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import org.lwjgl.glfw.GLFW;

public final class AutoHitCrystal
extends Module
implements TickListener,
ItemUseListener,
AttackListener {
    private final KeybindSetting activateKey = (KeybindSetting)new KeybindSetting(EncryptedString.of("Activate Key"), 1, false).setDescription(EncryptedString.of("Key that does hit crystalling"));
    private final BooleanSetting checkPlace = (BooleanSetting)new BooleanSetting(EncryptedString.of("Check Place"), false).setDescription(EncryptedString.of("Checks if you can place the obsidian on that block"));
    private final NumberSetting switchDelay = new NumberSetting(EncryptedString.of("Switch Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting switchChance = new NumberSetting(EncryptedString.of("Switch Chance"), 0.0, 100.0, 100.0, 1.0);
    private final NumberSetting placeDelay = new NumberSetting(EncryptedString.of("Place Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting placeChance = (NumberSetting)new NumberSetting(EncryptedString.of("Place Chance"), 0.0, 100.0, 100.0, 1.0).setDescription(EncryptedString.of("Randomization"));
    private final BooleanSetting workWithTotem = new BooleanSetting(EncryptedString.of("Work With Totem"), false);
    private final BooleanSetting workWithCrystal = new BooleanSetting(EncryptedString.of("Work With Crystal"), false);
    private final BooleanSetting clickSimulation = (BooleanSetting)new BooleanSetting(EncryptedString.of("Click Simulation"), false).setDescription(EncryptedString.of("Makes the CPS hud think you're legit"));
    private final BooleanSetting swordSwap = new BooleanSetting(EncryptedString.of("Sword Swap"), true);
    private int placeClock = 0;
    private int switchClock = 0;
    private boolean active;
    private boolean crystalling;
    private boolean crystalSelected;

    public AutoHitCrystal() {
        super(EncryptedString.of("Auto Hit Crystal"), EncryptedString.of("Automatically hit-crystals for you"), -1, Category.COMBAT);
        this.addSettings(this.activateKey, this.checkPlace, this.switchDelay, this.switchChance, this.placeDelay, this.placeChance, this.workWithTotem, this.workWithCrystal, this.clickSimulation, this.swordSwap);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.eventManager.add(ItemUseListener.class, this);
        this.eventManager.add(AttackListener.class, this);
        this.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        this.eventManager.remove(ItemUseListener.class, this);
        this.eventManager.remove(AttackListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        int randomNum = MathUtils.randomInt(1, 100);
        if (this.mc.field_1755 != null) {
            return;
        }
        if (KeyUtils.isKeyPressed(this.activateKey.getKey())) {
            class_1799 mainHandStack;
            class_239 class_2392 = this.mc.field_1765;
            if (class_2392 instanceof class_3965) {
                class_3965 hitResult = (class_3965)class_2392;
                if (this.mc.field_1765.method_17783() == class_239.class_240.field_1332 && !this.active && !BlockUtils.canPlaceBlockClient(hitResult.method_17777()) && this.checkPlace.getValue()) {
                    return;
                }
            }
            if (!((mainHandStack = this.mc.field_1724.method_6047()).method_7909() instanceof class_1829 || this.workWithTotem.getValue() && mainHandStack.method_31574(class_1802.field_8288) || this.workWithCrystal.getValue() && mainHandStack.method_31574(class_1802.field_8301) || this.active)) {
                return;
            }
            class_239 class_2393 = this.mc.field_1765;
            if (class_2393 instanceof class_3965) {
                class_3965 hitResult = (class_3965)class_2393;
                if (!this.active && this.swordSwap.getValue() && this.mc.field_1765.method_17783() == class_239.class_240.field_1332) {
                    class_2248 block = this.mc.field_1687.method_8320(hitResult.method_17777()).method_26204();
                    this.crystalling = block == class_2246.field_10540 || block == class_2246.field_9987;
                }
            }
            this.active = true;
            if (!this.crystalling && (class_2393 = this.mc.field_1765) instanceof class_3965) {
                class_3965 hit = (class_3965)class_2393;
                if (hit.method_17783() == class_239.class_240.field_1333) {
                    return;
                }
                if (!BlockUtils.isBlock(hit.method_17777(), class_2246.field_10540)) {
                    if (BlockUtils.isBlock(hit.method_17777(), class_2246.field_23152) && BlockUtils.isAnchorCharged(hit.method_17777())) {
                        return;
                    }
                    this.mc.field_1690.field_1904.method_23481(false);
                    if (!this.mc.field_1724.method_24518(class_1802.field_8281)) {
                        if (this.switchClock > 0) {
                            --this.switchClock;
                            return;
                        }
                        if (randomNum <= this.switchChance.getValueInt()) {
                            this.switchClock = this.switchDelay.getValueInt();
                            InventoryUtils.selectItemFromHotbar(class_1802.field_8281);
                        }
                    }
                    if (this.mc.field_1724.method_24518(class_1802.field_8281)) {
                        if (this.placeClock > 0) {
                            --this.placeClock;
                            return;
                        }
                        if (this.clickSimulation.getValue()) {
                            MouseSimulation.mouseClick(1);
                        }
                        if ((randomNum = MathUtils.randomInt(1, 100)) <= this.placeChance.getValueInt()) {
                            WorldUtils.placeBlock(hit, true);
                            this.placeClock = this.placeDelay.getValueInt();
                            this.crystalling = true;
                        }
                    }
                }
            }
            if (this.crystalling) {
                AutoCrystal autoCrystal;
                if (!this.mc.field_1724.method_24518(class_1802.field_8301) && !this.crystalSelected) {
                    if (this.switchClock > 0) {
                        --this.switchClock;
                        return;
                    }
                    randomNum = MathUtils.randomInt(1, 100);
                    if (randomNum <= this.switchChance.getValueInt()) {
                        this.crystalSelected = InventoryUtils.selectItemFromHotbar(class_1802.field_8301);
                        this.switchClock = this.switchDelay.getValueInt();
                    }
                }
                if (this.mc.field_1724.method_24518(class_1802.field_8301) && !(autoCrystal = Argon.INSTANCE.getModuleManager().getModule(AutoCrystal.class)).isEnabled()) {
                    autoCrystal.onTick();
                }
            }
        } else {
            this.reset();
        }
    }

    @Override
    public void onItemUse(ItemUseListener.ItemUseEvent event) {
        class_1799 mainHandStack = this.mc.field_1724.method_6047();
        if ((mainHandStack.method_31574(class_1802.field_8301) || mainHandStack.method_31574(class_1802.field_8281)) && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)1) != 1) {
            event.cancel();
        }
    }

    public void reset() {
        this.placeClock = this.placeDelay.getValueInt();
        this.switchClock = this.switchDelay.getValueInt();
        this.active = false;
        this.crystalling = false;
        this.crystalSelected = false;
    }

    @Override
    public void onAttack(AttackListener.AttackEvent event) {
        if (this.mc.field_1724.method_6047().method_31574(class_1802.field_8301) && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)0) != 1) {
            event.cancel();
        }
    }
}
