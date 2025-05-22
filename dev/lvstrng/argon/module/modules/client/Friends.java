package dev.lvstrng.argon.module.modules.client;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.AttackListener;
import dev.lvstrng.argon.event.events.ButtonListener;
import dev.lvstrng.argon.event.events.HudListener;
import dev.lvstrng.argon.managers.FriendManager;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.KeybindSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.WorldUtils;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_332;
import net.minecraft.class_3966;

public final class Friends
extends Module
implements ButtonListener,
AttackListener,
HudListener {
    private final KeybindSetting addFriendKey = (KeybindSetting)new KeybindSetting(EncryptedString.of("Friend Key"), 2, false).setDescription(EncryptedString.of("Key to add/remove friends"));
    public final BooleanSetting antiAttack = (BooleanSetting)new BooleanSetting(EncryptedString.of("Anti-Attack"), false).setDescription(EncryptedString.of("Doesn't let you hit friends"));
    public final BooleanSetting disableAimAssist = (BooleanSetting)new BooleanSetting(EncryptedString.of("Anti-Aim"), false).setDescription(EncryptedString.of("Disables aim assist for friends"));
    public final BooleanSetting friendStatus = (BooleanSetting)new BooleanSetting(EncryptedString.of("Friend Status"), false).setDescription(EncryptedString.of("Tells you if you're aiming at a friend or not"));
    private FriendManager manager;

    public Friends() {
        super(EncryptedString.of("Friends"), EncryptedString.of("This module makes it so you can't do certain stuff if you have a player friended!"), -1, Category.CLIENT);
        this.addSettings(this.addFriendKey, this.antiAttack, this.disableAimAssist, this.friendStatus);
        this.setKey(-1);
    }

    @Override
    public void onEnable() {
        this.manager = Argon.INSTANCE.getFriendManager();
        this.eventManager.add(ButtonListener.class, this);
        this.eventManager.add(AttackListener.class, this);
        this.eventManager.add(HudListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(ButtonListener.class, this);
        this.eventManager.remove(AttackListener.class, this);
        this.eventManager.remove(HudListener.class, this);
        super.onDisable();
    }

    @Override
    public void onButtonPress(ButtonListener.ButtonEvent event) {
        class_3966 hitResult;
        class_1297 entity;
        if (this.mc.field_1724 == null) {
            return;
        }
        if (this.mc.field_1755 != null) {
            return;
        }
        class_239 class_2392 = this.mc.field_1765;
        if (class_2392 instanceof class_3966 && (entity = (hitResult = (class_3966)class_2392).method_17782()) instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            if (event.button == this.addFriendKey.getKey() && event.action == 1) {
                if (!this.manager.isFriend(player)) {
                    this.manager.addFriend(player);
                } else {
                    this.manager.removeFriend(player);
                }
            }
        }
    }

    @Override
    public void onAttack(AttackListener.AttackEvent event) {
        if (!this.antiAttack.getValue()) {
            return;
        }
        if (this.manager.isAimingOverFriend()) {
            event.cancel();
        }
    }

    @Override
    public void onRenderHud(HudListener.HudEvent event) {
        if (!this.friendStatus.getValue()) {
            return;
        }
        RenderUtils.unscaledProjection();
        class_239 class_2392 = WorldUtils.getHitResult(100.0);
        if (class_2392 instanceof class_3966) {
            class_1657 player;
            class_3966 hitResult = (class_3966)class_2392;
            class_1297 entity = hitResult.method_17782();
            class_332 context = event.context;
            if (entity instanceof class_1657 && this.manager.isFriend(player = (class_1657)entity)) {
                TextRenderer.drawCenteredString(EncryptedString.of("Player is friend"), context, this.mc.method_22683().method_4480() / 2, this.mc.method_22683().method_4507() / 2 + 25, Color.GREEN.getRGB());
            }
        }
        RenderUtils.scaledProjection();
    }
}
