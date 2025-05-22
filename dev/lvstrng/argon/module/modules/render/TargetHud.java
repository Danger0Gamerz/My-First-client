package dev.lvstrng.argon.module.modules.render;

import dev.lvstrng.argon.event.events.HudListener;
import dev.lvstrng.argon.event.events.PacketSendListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import java.awt.Color;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_640;
import net.minecraft.class_7532;

public final class TargetHud
extends Module
implements HudListener,
PacketSendListener {
    private final NumberSetting xCoord = new NumberSetting(EncryptedString.of("X"), 0.0, 1920.0, 700.0, 1.0);
    private final NumberSetting yCoord = new NumberSetting(EncryptedString.of("Y"), 0.0, 1080.0, 600.0, 1.0);
    private final BooleanSetting hudTimeout = (BooleanSetting)new BooleanSetting(EncryptedString.of("Timeout"), true).setDescription(EncryptedString.of("Target hud will disappear after 10 seconds"));
    private long lastAttackTime = 0L;
    public static float animation;
    private static final long timeout = 10000L;
    private float healthAnimation = 0.0f;
    private float damageAnimation = 0.0f;
    private long lastDamageTime = 0L;
    private float opacity = 0.0f;
    private class_1657 lastTarget = null;
    private float initialAnimation = 0.0f;
    private boolean isFirstRender = true;
    private float scaleAnimation = 0.0f;

    public TargetHud() {
        super(EncryptedString.of("Target HUD"), EncryptedString.of("Gives you information about the enemy player"), -1, Category.RENDER);
        this.addSettings(this.xCoord, this.yCoord, this.hudTimeout);
    }

    @Override
    public void onEnable() {
        this.opacity = 0.0f;
        animation = 0.0f;
        this.healthAnimation = 0.0f;
        this.damageAnimation = 0.0f;
        this.initialAnimation = 0.0f;
        this.isFirstRender = true;
        this.lastTarget = null;
        this.scaleAnimation = 0.0f;
        this.eventManager.add(HudListener.class, this);
        this.eventManager.add(PacketSendListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(HudListener.class, this);
        this.eventManager.remove(PacketSendListener.class, this);
        super.onDisable();
    }

    @Override
    public void onRenderHud(HudListener.HudEvent event) {
        class_1657 player;
        class_1309 class_13092;
        class_332 context = event.context;
        int x = this.xCoord.getValueInt();
        int y = this.yCoord.getValueInt();
        RenderUtils.unscaledProjection();
        if ((!this.hudTimeout.getValue() || System.currentTimeMillis() - this.lastAttackTime <= 10000L) && this.mc.field_1724.method_6052() != null && (class_13092 = this.mc.field_1724.method_6052()) instanceof class_1657 && (player = (class_1657)class_13092).method_5805()) {
            if (this.isFirstRender) {
                this.healthAnimation = player.method_6032() / player.method_6063();
                this.opacity = 0.0f;
                animation = 0.0f;
                this.scaleAnimation = 0.0f;
                this.isFirstRender = false;
            }
            this.scaleAnimation = RenderUtils.fast(this.scaleAnimation, 1.0f, 40.0f);
            class_640 entry = this.mc.method_1562().method_2871(player.method_5667());
            class_4587 matrixStack = context.method_51448();
            matrixStack.method_22903();
            float centerX = x + 120;
            float centerY = y + 42;
            matrixStack.method_46416(centerX, centerY, 0.0f);
            matrixStack.method_22905(this.scaleAnimation, this.scaleAnimation, 1.0f);
            matrixStack.method_46416(-centerX, -centerY, 0.0f);
            String playerName = player.method_5477().getString();
            String pingText = entry != null ? entry.method_2959() + "ms" : "0ms";
            int pingWidth = this.mc.field_1772.method_1727(pingText) + 32;
            int nameWidth = Math.max(240, Math.min(300, this.mc.field_1772.method_1727(playerName) + 150 + pingWidth));
            Color bgColor = new Color(20, 20, 25, 230);
            Color accentColor = new Color(255, 255, 255, 25);
            Color borderColor = new Color(255, 255, 255, 30);
            Color textColor = new Color(255, 255, 255, (int)(255.0f * this.opacity * this.scaleAnimation));
            RenderUtils.renderBlurredBackground(context.method_51448(), x, y, nameWidth, 85.0f, new Color(20, 20, 25, 100));
            RenderUtils.renderDropShadow(context.method_51448(), x, y, nameWidth, 85.0f, 6.0f, new Color(0, 0, 0, 160));
            RenderUtils.renderRoundedQuad(context.method_51448(), new Color(20, 20, 25, 200), x, y, x + nameWidth, y + 85, 8.0, 8.0, 8.0, 8.0, 15.0);
            RenderUtils.renderRoundedQuad(context.method_51448(), accentColor, x, y, x + nameWidth, y + 42, 8.0, 8.0, 0.0, 0.0, 15.0);
            if (entry != null) {
                RenderUtils.renderRoundedQuad(context.method_51448(), borderColor, x + 8, y + 8, x + 34, y + 34, 5.0, 5.0, 5.0, 5.0, 10.0);
                class_7532.method_44443((class_332)context, (class_2960)entry.method_52810().comp_1626(), (int)(x + 9), (int)(y + 9), (int)24);
            }
            TextRenderer.drawString(playerName, context, x + 42, y + 12, textColor.getRGB());
            int ping = entry != null ? entry.method_2959() : 0;
            Color pingColor = this.getPingColor(ping);
            TextRenderer.drawString(pingText, context, x + nameWidth - this.mc.field_1772.method_1727(pingText) - 32, y + 12, pingColor.getRGB());
            RenderUtils.renderRoundedQuad(context.method_51448(), new Color(0, 0, 0, 100), x + 42, y + 28, x + nameWidth - 8, y + 36, 3.0, 3.0, 3.0, 3.0, 10.0);
            float targetHealth = (player.method_6032() + player.method_6067()) / player.method_6063();
            this.healthAnimation = RenderUtils.fast(this.healthAnimation, targetHealth, 10.0f);
            int healthWidth = (int)((float)(nameWidth - 50) * this.healthAnimation);
            Color healthColor = this.getHealthColor(this.healthAnimation);
            RenderUtils.renderRoundedQuad(context.method_51448(), healthColor, x + 42, y + 28, x + 42 + healthWidth, y + 36, 3.0, 3.0, 3.0, 3.0, 10.0);
            String healthText = String.format("%.1f\u2764", Float.valueOf(player.method_6032() + player.method_6067()));
            TextRenderer.drawString(healthText, context, x + 42, y + 50, healthColor.getRGB());
            String distanceText = String.format("%.1fm", Float.valueOf(player.method_5739((class_1297)this.mc.field_1724)));
            TextRenderer.drawString(distanceText, context, x + 100, y + 50, Color.WHITE.getRGB());
            if (player.field_6235 > 0) {
                this.damageAnimation = 1.0f;
                this.lastDamageTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - this.lastDamageTime > 500L) {
                this.damageAnimation = RenderUtils.fast(this.damageAnimation, 0.0f, 5.0f);
            }
            if (this.damageAnimation > 0.0f) {
                RenderUtils.renderRoundedQuad(context.method_51448(), new Color(255, 0, 0, (int)(60.0f * this.damageAnimation)), x, y, x + nameWidth, y + 85, 8.0, 8.0, 8.0, 8.0, 15.0);
            }
            matrixStack.method_22909();
        } else {
            this.scaleAnimation = RenderUtils.fast(this.scaleAnimation, 0.0f, 40.0f);
            if (this.scaleAnimation <= 0.01f) {
                this.lastTarget = null;
                this.isFirstRender = true;
            }
        }
        RenderUtils.scaledProjection();
    }

    private Color getHealthColor(float health) {
        return new Color((int)(255.0f * (1.0f - health)), (int)(255.0f * (0.8f + health * 0.2f)), (int)(255.0f * (health * 0.3f)), 255);
    }

    private Color getPingColor(int ping) {
        if (ping < 50) {
            return new Color(0, 255, 0);
        }
        if (ping < 100) {
            return new Color(255, 255, 0);
        }
        if (ping < 150) {
            return new Color(255, 150, 0);
        }
        return new Color(255, 0, 0);
    }

    @Override
    public void onPacketSend(PacketSendListener.PacketSendEvent event) {
        class_2596 class_25962 = event.packet;
        if (class_25962 instanceof class_2824) {
            class_2824 packet = (class_2824)class_25962;
            packet.method_34209(new class_2824.class_5908(){

                public void method_34219(class_1268 hand) {
                }

                public void method_34220(class_1268 hand, class_243 pos) {
                }

                public void method_34218() {
                    if (((TargetHud)TargetHud.this).mc.field_1692 instanceof class_1657) {
                        TargetHud.this.lastAttackTime = System.currentTimeMillis();
                    }
                }
            });
        }
    }
}
