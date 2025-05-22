package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.AttackListener;
import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.Friends;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.MouseSimulation;
import dev.lvstrng.argon.utils.TimerUtils;
import dev.lvstrng.argon.utils.WorldUtils;
import net.minecraft.class_1297;
import net.minecraft.class_1642;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_1819;
import net.minecraft.class_1829;
import net.minecraft.class_239;
import net.minecraft.class_3966;
import net.minecraft.class_9334;
import org.lwjgl.glfw.GLFW;

public final class TriggerBot
extends Module
implements TickListener,
AttackListener {
    private final BooleanSetting inScreen = (BooleanSetting)new BooleanSetting(EncryptedString.of("Work In Screen"), false).setDescription(EncryptedString.of("Will trigger even if youre inside a screen"));
    private final BooleanSetting whileUse = (BooleanSetting)new BooleanSetting(EncryptedString.of("While Use"), false).setDescription(EncryptedString.of("Will hit the player no matter if you're eating or blocking with a shield"));
    private final BooleanSetting onLeftClick = (BooleanSetting)new BooleanSetting(EncryptedString.of("On Left Click"), false).setDescription(EncryptedString.of("Only gets triggered if holding down left click"));
    private final BooleanSetting allItems = (BooleanSetting)new BooleanSetting(EncryptedString.of("All Items"), false).setDescription(EncryptedString.of("Works with all Items /THIS USES SWORD DELAY AS THE DELAY/"));
    private final MinMaxSetting swordDelay = (MinMaxSetting)new MinMaxSetting(EncryptedString.of("Sword Delay"), 0.0, 1000.0, 1.0, 540.0, 550.0).setDescription(EncryptedString.of("Delay for swords"));
    private final MinMaxSetting axeDelay = (MinMaxSetting)new MinMaxSetting(EncryptedString.of("Axe Delay"), 0.0, 1000.0, 1.0, 780.0, 800.0).setDescription(EncryptedString.of("Delay for axes"));
    private final BooleanSetting checkShield = (BooleanSetting)new BooleanSetting(EncryptedString.of("Check Shield"), false).setDescription(EncryptedString.of("Checks if the player is blocking your hits with a shield (Recommended with Shield Disabler)"));
    private final BooleanSetting onlyCritSword = (BooleanSetting)new BooleanSetting(EncryptedString.of("Only Crit Sword"), false).setDescription(EncryptedString.of("Only does critical hits with a sword"));
    private final BooleanSetting onlyCritAxe = (BooleanSetting)new BooleanSetting(EncryptedString.of("Only Crit Axe"), false).setDescription(EncryptedString.of("Only does critical hits with an axe"));
    private final BooleanSetting swing = (BooleanSetting)new BooleanSetting(EncryptedString.of("Swing Hand"), true).setDescription(EncryptedString.of("Whether to swing the hand or not"));
    private final BooleanSetting whileAscend = (BooleanSetting)new BooleanSetting(EncryptedString.of("While Ascending"), false).setDescription(EncryptedString.of("Wont hit if you're ascending from a jump, only if on ground or falling"));
    private final BooleanSetting clickSimulation = (BooleanSetting)new BooleanSetting(EncryptedString.of("Click Simulation"), false).setDescription(EncryptedString.of("Makes the CPS hud think you're legit"));
    private final BooleanSetting strayBypass = (BooleanSetting)new BooleanSetting(EncryptedString.of("Stray Bypass"), false).setDescription(EncryptedString.of("Bypasses stray's Anti-TriggerBot"));
    private final BooleanSetting allEntities = (BooleanSetting)new BooleanSetting(EncryptedString.of("All Entities"), false).setDescription(EncryptedString.of("Will attack all entities"));
    private final BooleanSetting useShield = (BooleanSetting)new BooleanSetting(EncryptedString.of("Use Shield"), false).setDescription(EncryptedString.of("Uses shield if it's in your offhand"));
    private final NumberSetting shieldTime = new NumberSetting(EncryptedString.of("Shield Time"), 100.0, 1000.0, 350.0, 1.0);
    private final BooleanSetting sticky = (BooleanSetting)new BooleanSetting(EncryptedString.of("Same Player"), false).setDescription(EncryptedString.of("Hits the player that was recently attacked, good for FFA"));
    private final TimerUtils timer = new TimerUtils();
    private int currentSwordDelay;
    private int currentAxeDelay;

    public TriggerBot() {
        super(EncryptedString.of("Trigger Bot"), EncryptedString.of("Automatically hits players for you"), -1, Category.COMBAT);
        this.addSettings(this.inScreen, this.whileUse, this.onLeftClick, this.allItems, this.swordDelay, this.axeDelay, this.checkShield, this.whileAscend, this.sticky, this.onlyCritSword, this.onlyCritAxe, this.swing, this.clickSimulation, this.strayBypass, this.allEntities, this.useShield, this.shieldTime);
    }

    @Override
    public void onEnable() {
        this.currentSwordDelay = this.swordDelay.getRandomValueInt();
        this.currentAxeDelay = this.axeDelay.getRandomValueInt();
        this.eventManager.add(TickListener.class, this);
        this.eventManager.add(AttackListener.class, this);
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
        try {
            if (!this.inScreen.getValue() && this.mc.field_1755 != null) {
                return;
            }
            if (Argon.INSTANCE.getModuleManager().getModule(Friends.class).antiAttack.getValue() && Argon.INSTANCE.getFriendManager().isAimingOverFriend()) {
                return;
            }
            class_1792 item = this.mc.field_1724.method_6047().method_7909();
            if (this.onLeftClick.getValue() && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)0) != 1) {
                return;
            }
            if ((this.mc.field_1724.method_6079().method_7909().method_57347().method_57832(class_9334.field_50075) || this.mc.field_1724.method_6079().method_7909() instanceof class_1819) && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)1) == 1 && !this.whileUse.getValue()) {
                return;
            }
            if (!this.whileAscend.getValue() && (!this.mc.field_1724.method_24828() && this.mc.field_1724.method_18798().field_1351 > 0.0 || !this.mc.field_1724.method_24828() && this.mc.field_1724.field_6017 <= 0.0f)) {
                return;
            }
            if (!this.allItems.getValue()) {
                class_3966 hit;
                class_239 entity2;
                if (item instanceof class_1829) {
                    class_239 class_2392 = this.mc.field_1765;
                    if (class_2392 instanceof class_3966) {
                        class_3966 hit2 = (class_3966)class_2392;
                        class_1297 entity2 = hit2.method_17782();
                        assert (this.mc.field_1724.method_6052() != null);
                        if (this.sticky.getValue() && entity2 != this.mc.field_1724.method_6052()) {
                            return;
                        }
                        if (entity2 instanceof class_1657 || this.strayBypass.getValue() && entity2 instanceof class_1642 || this.allEntities.getValue() && entity2 != null) {
                            if (entity2 instanceof class_1657) {
                                class_1657 player = (class_1657)entity2;
                                if (this.checkShield.getValue() && player.method_6039() && !WorldUtils.isShieldFacingAway(player)) {
                                    return;
                                }
                            }
                            if (this.onlyCritSword.getValue() && this.mc.field_1724.field_6017 <= 0.0f) {
                                return;
                            }
                            if (this.timer.delay(this.currentSwordDelay)) {
                                if (this.useShield.getValue() && this.mc.field_1724.method_6079().method_7909() == class_1802.field_8255 && this.mc.field_1724.method_6039()) {
                                    MouseSimulation.mouseRelease(1);
                                }
                                WorldUtils.hitEntity(entity2, this.swing.getValue());
                                if (this.clickSimulation.getValue()) {
                                    MouseSimulation.mouseClick(0);
                                }
                                this.currentSwordDelay = this.swordDelay.getRandomValueInt();
                                this.timer.reset();
                            } else if (this.useShield.getValue() && this.mc.field_1724.method_6079().method_7909() == class_1802.field_8255) {
                                int useFor = this.shieldTime.getValueInt();
                                MouseSimulation.mouseClick(1, useFor);
                            }
                        }
                    }
                } else if (item instanceof class_1743 && (entity2 = this.mc.field_1765) instanceof class_3966 && ((entity2 = (hit = (class_3966)entity2).method_17782()) instanceof class_1657 || this.strayBypass.getValue() && entity2 instanceof class_1642 || this.allEntities.getValue() && entity2 != null)) {
                    if (entity2 instanceof class_1657) {
                        class_1657 player = (class_1657)entity2;
                        if (this.checkShield.getValue() && player.method_6039() && !WorldUtils.isShieldFacingAway(player)) {
                            return;
                        }
                    }
                    if (this.onlyCritAxe.getValue() && this.mc.field_1724.field_6017 <= 0.0f) {
                        return;
                    }
                    if (this.timer.delay(this.currentAxeDelay)) {
                        WorldUtils.hitEntity((class_1297)entity2, this.swing.getValue());
                        if (this.clickSimulation.getValue()) {
                            MouseSimulation.mouseClick(0);
                        }
                        this.currentAxeDelay = this.axeDelay.getRandomValueInt();
                        this.timer.reset();
                    } else if (this.useShield.getValue() && this.mc.field_1724.method_6079().method_7909() == class_1802.field_8255) {
                        int useFor = this.shieldTime.getValueInt();
                        MouseSimulation.mouseClick(1, useFor);
                    }
                }
            } else {
                class_239 entity = this.mc.field_1765;
                if (entity instanceof class_3966) {
                    class_3966 entityHit = (class_3966)entity;
                    if (this.mc.field_1765.method_17783() == class_239.class_240.field_1331) {
                        entity = entityHit.method_17782();
                        assert (this.mc.field_1724.method_6052() != null);
                        if (this.sticky.getValue() && entity != this.mc.field_1724.method_6052()) {
                            return;
                        }
                        if (entity instanceof class_1657 || this.strayBypass.getValue() && entity instanceof class_1642 || this.allEntities.getValue() && entity != null) {
                            if (entity instanceof class_1657) {
                                class_1657 player = (class_1657)entity;
                                if (this.checkShield.getValue() && player.method_6039() && !WorldUtils.isShieldFacingAway(player)) {
                                    return;
                                }
                            }
                            if (this.onlyCritSword.getValue() && this.mc.field_1724.field_6017 <= 0.0f) {
                                return;
                            }
                            if (this.timer.delay(this.currentSwordDelay)) {
                                WorldUtils.hitEntity((class_1297)entity, this.swing.getValue());
                                if (this.clickSimulation.getValue()) {
                                    MouseSimulation.mouseClick(0);
                                }
                                this.currentSwordDelay = this.swordDelay.getRandomValueInt();
                                this.timer.reset();
                            } else if (this.useShield.getValue() && this.mc.field_1724.method_6079().method_7909() == class_1802.field_8255) {
                                int useFor = this.shieldTime.getValueInt();
                                MouseSimulation.mouseClick(1, useFor);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onAttack(AttackListener.AttackEvent event) {
        if (GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)0) != 1) {
            event.cancel();
        }
    }
}
