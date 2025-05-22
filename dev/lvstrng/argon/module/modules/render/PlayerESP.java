package dev.lvstrng.argon.module.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lvstrng.argon.event.events.GameRenderListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class PlayerESP
extends Module
implements GameRenderListener {
    public final ModeSetting<Mode> mode = new ModeSetting<Mode>(EncryptedString.of("Mode"), Mode.ThreeD, Mode.class);
    private final NumberSetting alpha = new NumberSetting(EncryptedString.of("Alpha"), 0.0, 255.0, 100.0, 1.0);
    private final NumberSetting width = new NumberSetting(EncryptedString.of("Line width"), 1.0, 10.0, 1.0, 1.0);
    private final BooleanSetting tracers = (BooleanSetting)new BooleanSetting(EncryptedString.of("Tracers"), false).setDescription(EncryptedString.of("Draws a line from your player to the other"));

    public PlayerESP() {
        super(EncryptedString.of("Player ESP"), EncryptedString.of("Renders players through walls"), -1, Category.RENDER);
        this.addSettings(this.alpha, this.mode, this.width, this.tracers);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(GameRenderListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(GameRenderListener.class, this);
        super.onDisable();
    }

    @Override
    public void onGameRender(GameRenderListener.GameRenderEvent event) {
        for (class_1657 player : this.mc.field_1687.method_18456()) {
            class_243 vec;
            class_4184 cam;
            if (this.mode.isMode(Mode.ThreeD)) {
                if (player == this.mc.field_1724) continue;
                cam = this.mc.method_31975().field_4344;
                if (cam != null) {
                    class_4587 matrices = event.matrices;
                    matrices.method_22903();
                    vec = cam.method_19326();
                    matrices.method_22904(-vec.field_1352, -vec.field_1351, -vec.field_1350);
                }
                double xPos = class_3532.method_16436((double)class_9779.field_51956.method_60637(true), (double)player.field_6014, (double)player.method_23317());
                double yPos = class_3532.method_16436((double)class_9779.field_51956.method_60637(true), (double)player.field_6036, (double)player.method_23318());
                double zPos = class_3532.method_16436((double)class_9779.field_51956.method_60637(true), (double)player.field_5969, (double)player.method_23321());
                RenderUtils.renderFilledBox(event.matrices, (float)xPos - player.method_17681() / 2.0f, (float)yPos, (float)zPos - player.method_17681() / 2.0f, (float)xPos + player.method_17681() / 2.0f, (float)yPos + player.method_17682(), (float)zPos + player.method_17681() / 2.0f, Utils.getMainColor(this.alpha.getValueInt(), 1).brighter());
                if (this.tracers.getValue()) {
                    RenderUtils.renderLine(event.matrices, Utils.getMainColor(255, 1), this.mc.field_1765.method_17784(), player.method_30950(class_9779.field_51956.method_60637(true)));
                }
                event.matrices.method_22909();
                continue;
            }
            if (!this.mode.isMode(Mode.TwoD) || player == this.mc.field_1724) continue;
            this.renderOutline(player, this.getColor(this.alpha.getValueInt()), event.matrices);
            cam = this.mc.method_31975().field_4344;
            if (cam != null) {
                class_4587 matrices = event.matrices;
                matrices.method_22903();
                vec = cam.method_19326();
                matrices.method_22904(-vec.field_1352, -vec.field_1351, -vec.field_1350);
            }
            if (this.tracers.getValue()) {
                RenderUtils.renderLine(event.matrices, Utils.getMainColor(255, 1), this.mc.field_1765.method_17784(), player.method_30950(class_9779.field_51956.method_60637(true)));
            }
            event.matrices.method_22909();
        }
    }

    private void renderOutline(class_1657 e, Color color, class_4587 stack) {
        float red = (float)color.brighter().getRed() / 255.0f;
        float green = (float)color.brighter().getGreen() / 255.0f;
        float blue = (float)color.brighter().getBlue() / 255.0f;
        float alpha = (float)color.brighter().getAlpha() / 255.0f;
        class_4184 c = this.mc.field_1773.method_19418();
        class_243 camPos = c.method_19326();
        class_243 start = e.method_30950(class_9779.field_51956.method_60637(true)).method_1020(camPos);
        float x = (float)start.field_1352;
        float y = (float)start.field_1351;
        float z = (float)start.field_1350;
        double r = Math.toRadians(-c.method_19330() + 90.0f);
        float sin = (float)(Math.sin(r) * ((double)e.method_17681() / 1.7));
        float cos = (float)(Math.cos(r) * ((double)e.method_17681() / 1.7));
        stack.method_22903();
        Matrix4f matrix = stack.method_23760().method_23761();
        RenderSystem.setShader(class_757::method_34540);
        if (ClickGUI.antiAliasing.getValue()) {
            GL11.glEnable((int)32925);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
        }
        GL11.glDepthFunc((int)519);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        GL11.glLineWidth((float)this.width.getValueInt());
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        buffer.method_22918(matrix, x + sin, y, z + cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x - sin, y, z - cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x - sin, y, z - cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x - sin, y + e.method_17682(), z - cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x - sin, y + e.method_17682(), z - cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x + sin, y + e.method_17682(), z + cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x + sin, y + e.method_17682(), z + cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x + sin, y, z + cos).method_22915(red, green, blue, alpha);
        buffer.method_22918(matrix, x + sin, y, z + cos).method_22915(red, green, blue, alpha);
        class_286.method_43433((class_9801)buffer.method_60800());
        GL11.glDepthFunc((int)515);
        GL11.glLineWidth((float)1.0f);
        RenderSystem.disableBlend();
        if (ClickGUI.antiAliasing.getValue()) {
            GL11.glDisable((int)2848);
            GL11.glDisable((int)32925);
        }
        stack.method_22909();
    }

    private Color getColor(int alpha) {
        int red = ClickGUI.red.getValueInt();
        int green = ClickGUI.green.getValueInt();
        int blue = ClickGUI.blue.getValueInt();
        if (ClickGUI.rainbow.getValue()) {
            return ColorUtils.getBreathingRGBColor(1, alpha);
        }
        return new Color(red, green, blue, alpha);
    }

    public static enum Mode {
        TwoD,
        ThreeD;

    }
}
