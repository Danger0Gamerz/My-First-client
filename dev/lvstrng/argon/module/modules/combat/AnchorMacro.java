package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.ItemUseListener;
import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.BlockUtils;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.InventoryUtils;
import dev.lvstrng.argon.utils.KeyUtils;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.MouseSimulation;
import dev.lvstrng.argon.utils.WorldUtils;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1802;
import net.minecraft.class_1819;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_9334;
import org.lwjgl.glfw.GLFW;

public final class AnchorMacro
extends Module
implements TickListener,
ItemUseListener {
    private final BooleanSetting whileUse = (BooleanSetting)new BooleanSetting(EncryptedString.of("While Use"), true).setDescription(EncryptedString.of("If it should trigger while eating/using shield"));
    private final BooleanSetting stopOnKill = (BooleanSetting)new BooleanSetting(EncryptedString.of("Stop on Kill"), false).setDescription(EncryptedString.of("Doesn't anchor if body nearby"));
    private final BooleanSetting clickSimulation = (BooleanSetting)new BooleanSetting(EncryptedString.of("Click Simulation"), false).setDescription(EncryptedString.of("Makes the CPS hud think you're legit"));
    private final NumberSetting switchDelay = new NumberSetting(EncryptedString.of("Switch Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting switchChance = new NumberSetting(EncryptedString.of("Switch Chance"), 0.0, 100.0, 100.0, 1.0);
    private final NumberSetting placeChance = (NumberSetting)new NumberSetting(EncryptedString.of("Place Chance"), 0.0, 100.0, 100.0, 1.0).setDescription(EncryptedString.of("Randomization"));
    private final NumberSetting glowstoneDelay = new NumberSetting(EncryptedString.of("Glowstone Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting glowstoneChance = new NumberSetting(EncryptedString.of("Glowstone Chance"), 0.0, 100.0, 100.0, 1.0);
    private final NumberSetting explodeDelay = new NumberSetting(EncryptedString.of("Explode Delay"), 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting explodeChance = new NumberSetting(EncryptedString.of("Explode Chance"), 0.0, 100.0, 100.0, 1.0);
    private final NumberSetting explodeSlot = new NumberSetting(EncryptedString.of("Explode Slot"), 1.0, 9.0, 1.0, 1.0);
    private final BooleanSetting onlyOwn = new BooleanSetting(EncryptedString.of("Only Own"), false);
    private final BooleanSetting onlyCharge = new BooleanSetting(EncryptedString.of("Only Charge"), false);
    private int switchClock = 0;
    private int glowstoneClock = 0;
    private int explodeClock = 0;
    private final Set<class_2338> ownedAnchors = new HashSet<class_2338>();

    public AnchorMacro() {
        super(EncryptedString.of("Anchor Macro"), EncryptedString.of("Automatically blows up respawn anchors for you"), -1, Category.COMBAT);
        this.addSettings(this.whileUse, this.stopOnKill, this.clickSimulation, this.placeChance, this.switchDelay, this.switchChance, this.glowstoneDelay, this.glowstoneChance, this.explodeDelay, this.explodeChance, this.explodeSlot, this.onlyOwn, this.onlyCharge);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.eventManager.add(ItemUseListener.class, this);
        this.switchClock = 0;
        this.glowstoneClock = 0;
        this.explodeClock = 0;
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
        class_3965 hit;
        class_239 class_2392;
        if (this.mc.field_1755 != null) {
            return;
        }
        if ((this.mc.field_1724.method_6047().method_7909().method_57347().method_57832(class_9334.field_50075) || this.mc.field_1724.method_6047().method_7909() instanceof class_1819 || this.mc.field_1724.method_6079().method_7909() instanceof class_1819 || this.mc.field_1724.method_6079().method_7909().method_57347().method_57832(class_9334.field_50075)) && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)1) == 1 && !this.whileUse.getValue()) {
            return;
        }
        if (this.stopOnKill.getValue() && WorldUtils.isDeadBodyNearby()) {
            return;
        }
        int randomInt = MathUtils.randomInt(1, 100);
        if (KeyUtils.isKeyPressed(1) && (class_2392 = this.mc.field_1765) instanceof class_3965 && BlockUtils.isBlock((hit = (class_3965)class_2392).method_17777(), class_2246.field_23152)) {
            if (this.onlyOwn.getValue() && !this.ownedAnchors.contains(hit.method_17777())) {
                return;
            }
            this.mc.field_1690.field_1904.method_23481(false);
            if (BlockUtils.isAnchorNotCharged(hit.method_17777()) && (randomInt = MathUtils.randomInt(1, 100)) <= this.placeChance.getValueInt()) {
                if (!this.mc.field_1724.method_6047().method_31574(class_1802.field_8801)) {
                    if (this.switchClock != this.switchDelay.getValueInt()) {
                        ++this.switchClock;
                        return;
                    }
                    randomInt = MathUtils.randomInt(1, 100);
                    if (randomInt <= this.switchChance.getValueInt()) {
                        this.switchClock = 0;
                        InventoryUtils.selectItemFromHotbar(class_1802.field_8801);
                    }
                }
                if (this.mc.field_1724.method_6047().method_31574(class_1802.field_8801)) {
                    if (this.glowstoneClock != this.glowstoneDelay.getValueInt()) {
                        ++this.glowstoneClock;
                        return;
                    }
                    randomInt = MathUtils.randomInt(1, 100);
                    if (randomInt <= this.glowstoneChance.getValueInt()) {
                        this.glowstoneClock = 0;
                        if (this.clickSimulation.getValue()) {
                            MouseSimulation.mouseClick(1);
                        }
                        WorldUtils.placeBlock(hit, true);
                    }
                }
            }
            if (BlockUtils.isAnchorCharged(hit.method_17777())) {
                int slot = this.explodeSlot.getValueInt() - 1;
                if (this.mc.field_1724.method_31548().field_7545 != slot) {
                    if (this.switchClock != this.switchDelay.getValueInt()) {
                        ++this.switchClock;
                        return;
                    }
                    if (randomInt <= this.switchChance.getValueInt()) {
                        this.switchClock = 0;
                        this.mc.field_1724.method_31548().field_7545 = slot;
                    }
                }
                if (this.mc.field_1724.method_31548().field_7545 == slot) {
                    if (this.explodeClock != this.explodeDelay.getValueInt()) {
                        ++this.explodeClock;
                        return;
                    }
                    randomInt = MathUtils.randomInt(1, 100);
                    if (randomInt <= this.explodeChance.getValueInt()) {
                        this.explodeClock = 0;
                        if (!this.onlyCharge.getValue()) {
                            if (this.clickSimulation.getValue()) {
                                MouseSimulation.mouseClick(1);
                            }
                            WorldUtils.placeBlock(hit, true);
                            this.ownedAnchors.remove(hit.method_17777());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onItemUse(ItemUseListener.ItemUseEvent event) {
        class_3965 hitResult;
        class_239 class_2392 = this.mc.field_1765;
        if (class_2392 instanceof class_3965 && (hitResult = (class_3965)class_2392).method_17783() == class_239.class_240.field_1332) {
            class_2338 bp;
            if (this.mc.field_1724.method_6047().method_7909() == class_1802.field_23141) {
                class_2350 dir = hitResult.method_17780();
                class_2338 pos = hitResult.method_17777();
                if (!this.mc.field_1687.method_8320(pos).method_45474()) {
                    switch (dir) {
                        case field_11036: {
                            this.ownedAnchors.add(pos.method_10069(0, 1, 0));
                            break;
                        }
                        case field_11033: {
                            this.ownedAnchors.add(pos.method_10069(0, -1, 0));
                            break;
                        }
                        case field_11034: {
                            this.ownedAnchors.add(pos.method_10069(1, 0, 0));
                            break;
                        }
                        case field_11039: {
                            this.ownedAnchors.add(pos.method_10069(-1, 0, 0));
                            break;
                        }
                        case field_11043: {
                            this.ownedAnchors.add(pos.method_10069(0, 0, -1));
                            break;
                        }
                        case field_11035: {
                            this.ownedAnchors.add(pos.method_10069(0, 0, 1));
                        }
                    }
                } else {
                    this.ownedAnchors.add(pos);
                }
            }
            if (BlockUtils.isAnchorCharged(bp = hitResult.method_17777())) {
                this.ownedAnchors.remove(bp);
            }
        }
    }
}
