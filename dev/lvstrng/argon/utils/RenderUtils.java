package dev.lvstrng.argon.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_5944;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_8251;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public final class RenderUtils {
    public static class_8251 vertexSorter;
    public static boolean rendering3D;

    public static class_243 getCameraPos() {
        return Argon.mc.method_31975().field_4344.method_19326();
    }

    public static double deltaTime() {
        return Argon.mc.method_47599() > 0 ? 1.0 / (double)Argon.mc.method_47599() : 1.0;
    }

    public static float fast(float end, float start, float multiple) {
        return (1.0f - class_3532.method_15363((float)((float)(RenderUtils.deltaTime() * (double)multiple)), (float)0.0f, (float)1.0f)) * end + class_3532.method_15363((float)((float)(RenderUtils.deltaTime() * (double)multiple)), (float)0.0f, (float)1.0f) * start;
    }

    public static class_243 getPlayerLookVec(class_1657 player) {
        float f = (float)Math.PI / 180;
        float pi = (float)Math.PI;
        float f1 = class_3532.method_15362((float)(-player.method_36454() * f - pi));
        float f2 = class_3532.method_15374((float)(-player.method_36454() * f - pi));
        float f3 = -class_3532.method_15362((float)(-player.method_36455() * f));
        float f4 = class_3532.method_15374((float)(-player.method_36455() * f));
        return new class_243((double)(f2 * f3), (double)f4, (double)(f1 * f3)).method_1029();
    }

    public static void unscaledProjection() {
        vertexSorter = RenderSystem.getVertexSorting();
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f().setOrtho(0.0f, (float)Argon.mc.method_22683().method_4489(), (float)Argon.mc.method_22683().method_4506(), 0.0f, 1000.0f, 21000.0f), (class_8251)class_8251.field_43361);
        rendering3D = false;
    }

    public static void scaledProjection() {
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f().setOrtho(0.0f, (float)((double)Argon.mc.method_22683().method_4489() / Argon.mc.method_22683().method_4495()), (float)((double)Argon.mc.method_22683().method_4506() / Argon.mc.method_22683().method_4495()), 0.0f, 1000.0f, 21000.0f), (class_8251)vertexSorter);
        rendering3D = true;
    }

    public static void renderRoundedQuad(class_4587 matrices, Color c, double x, double y, double x2, double y2, double corner1, double corner2, double corner3, double corner4, double samples) {
        int color = c.getRGB();
        Matrix4f matrix = matrices.method_23760().method_23761();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.setShader(class_757::method_34540);
        RenderUtils.renderRoundedQuadInternal(matrix, g, h, k, f, x, y, x2, y2, corner1, corner2, corner3, corner4, samples);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void setup() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    private static void cleanup() {
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void renderRoundedQuad(class_4587 matrices, Color c, double x, double y, double x1, double y1, double rad, double samples) {
        RenderUtils.renderRoundedQuad(matrices, c, x, y, x1, y1, rad, rad, rad, rad, samples);
    }

    public static void renderRoundedOutlineInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radC1, double radC2, double radC3, double radC4, double width, double samples) {
        double rad;
        double[] current;
        int i;
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
        double[][] map = new double[][]{{toX - radC4, toY - radC4, radC4}, {toX - radC2, fromY + radC2, radC2}, {fromX + radC1, fromY + radC1, radC1}, {fromX + radC3, toY - radC3, radC3}};
        for (i = 0; i < 4; ++i) {
            current = map[i];
            rad = current[2];
            for (double r = (double)i * 90.0; r < 90.0 + (double)i * 90.0; r += 90.0 / samples) {
                float rad1 = (float)Math.toRadians(r);
                double sin1 = Math.sin(rad1);
                float sin = (float)(sin1 * rad);
                double cos1 = Math.cos(rad1);
                float cos = (float)(cos1 * rad);
                bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).method_22915(cr, cg, cb, ca);
                bufferBuilder.method_22918(matrix, (float)(current[0] + (double)sin + sin1 * width), (float)(current[1] + (double)cos + cos1 * width), 0.0f).method_22915(cr, cg, cb, ca);
            }
            float rad1 = (float)Math.toRadians(90.0 + (double)i * 90.0);
            double sin1 = Math.sin(rad1);
            float sin = (float)(sin1 * rad);
            double cos1 = Math.cos(rad1);
            float cos = (float)(cos1 * rad);
            bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).method_22915(cr, cg, cb, ca);
            bufferBuilder.method_22918(matrix, (float)(current[0] + (double)sin + sin1 * width), (float)(current[1] + (double)cos + cos1 * width), 0.0f).method_22915(cr, cg, cb, ca);
        }
        i = 0;
        current = map[i];
        rad = current[2];
        float cos = (float)rad;
        bufferBuilder.method_22918(matrix, (float)current[0], (float)current[1] + cos, 0.0f).method_22915(cr, cg, cb, ca);
        bufferBuilder.method_22918(matrix, (float)(current[0] + (double)cos), (float)(current[1] + (double)cos + width), 0.0f).method_22915(cr, cg, cb, ca);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
    }

    public static void setScissorRegion(int x, int y, int width, int height) {
        class_437 currentScreen = class_310.method_1551().field_1755;
        int screenHeight = currentScreen == null ? 0 : currentScreen.field_22790 - height;
        double scaleFactor = class_310.method_1551().method_22683().method_4495();
        GL11.glScissor((int)((int)((double)x * scaleFactor)), (int)((int)((double)screenHeight * scaleFactor)), (int)((int)((double)(width - x) * scaleFactor)), (int)((int)((double)(height - y) * scaleFactor)));
        GL11.glEnable((int)3089);
    }

    public static void renderCircle(class_4587 matrices, Color c, double originX, double originY, double rad, int segments) {
        int segments1 = class_3532.method_15340((int)segments, (int)4, (int)360);
        int color = c.getRGB();
        Matrix4f matrix = matrices.method_23760().method_23761();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        RenderUtils.setup();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27381, class_290.field_1576);
        for (int i = 0; i < 360; i += Math.min(360 / segments1, 360 - i)) {
            double radians = Math.toRadians(i);
            double sin = Math.sin(radians) * rad;
            double cos = Math.cos(radians) * rad;
            bufferBuilder.method_22918(matrix, (float)(originX + sin), (float)(originY + cos), 0.0f).method_22915(g, h, k, f);
        }
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderUtils.cleanup();
    }

    public static void renderShaderRect(class_4587 matrixStack, Color color, Color color2, Color color3, Color color4, float f, float f2, float f3, float f4, float f5, float f6) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1592);
        bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), f - 10.0f, f2 - 10.0f, 0.0f);
        bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), f - 10.0f, f2 + f4 + 20.0f, 0.0f);
        bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), f + f3 + 20.0f, f2 + f4 + 20.0f, 0.0f);
        bufferBuilder.method_22918(matrixStack.method_23760().method_23761(), f + f3 + 20.0f, f2 - 10.0f, 0.0f);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
    }

    public static void renderRoundedOutline(class_332 poses, Color c, double fromX, double fromY, double toX, double toY, double rad1, double rad2, double rad3, double rad4, double width, double samples) {
        int color = c.getRGB();
        Matrix4f matrix = poses.method_51448().method_23760().method_23761();
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        RenderUtils.setup();
        RenderSystem.setShader(class_757::method_34540);
        RenderUtils.renderRoundedOutlineInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad1, rad2, rad3, rad4, width, samples);
        RenderUtils.cleanup();
    }

    public static class_4587 matrixFrom(double x, double y, double z) {
        class_4587 matrices = new class_4587();
        class_4184 camera = class_310.method_1551().field_1773.method_19418();
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(x - camera.method_19326().field_1352, y - camera.method_19326().field_1351, z - camera.method_19326().field_1350);
        return matrices;
    }

    public static void renderQuad(class_4587 matrices, float x, float y, float width, float height, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        matrices.method_22903();
        matrices.method_22905(0.5f, 0.5f, 0.5f);
        matrices.method_22904((double)x, (double)y, 0.0);
        class_289 tessellator = class_289.method_1348();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        bufferBuilder.method_22912(0.0f, 0.0f, 0.0f).method_22915(red, green, blue, alpha);
        bufferBuilder.method_22912(0.0f, height, 0.0f).method_22915(red, green, blue, alpha);
        bufferBuilder.method_22912(width, height, 0.0f).method_22915(red, green, blue, alpha);
        bufferBuilder.method_22912(width, 0.0f, 0.0f).method_22915(red, green, blue, alpha);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.disableBlend();
        matrices.method_22909();
    }

    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double corner1, double corner2, double corner3, double corner4, double samples) {
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27381, class_290.field_1576);
        double[][] map = new double[][]{{toX - corner4, toY - corner4, corner4}, {toX - corner2, fromY + corner2, corner2}, {fromX + corner1, fromY + corner1, corner1}, {fromX + corner3, toY - corner3, corner3}};
        for (int i = 0; i < 4; ++i) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = (double)i * 90.0; r < 90.0 + (double)i * 90.0; r += 90.0 / samples) {
                float rad1 = (float)Math.toRadians(r);
                float sin = (float)(Math.sin(rad1) * rad);
                float cos = (float)(Math.cos(rad1) * rad);
                bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).method_22915(cr, cg, cb, ca);
            }
            float rad1 = (float)Math.toRadians(90.0 + (double)i * 90.0);
            float sin = (float)(Math.sin(rad1) * rad);
            float cos = (float)(Math.cos(rad1) * rad);
            bufferBuilder.method_22918(matrix, (float)current[0] + sin, (float)current[1] + cos, 0.0f).method_22915(cr, cg, cb, ca);
        }
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
    }

    public static void renderFilledBox(class_4587 matrices, float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        RenderSystem.setShader(class_757::method_34539);
        class_289 tessellator = class_289.method_1348();
        class_287 bufferBuilder = tessellator.method_60827(class_293.class_5596.field_27380, class_290.field_1592);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f2, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f3);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f6);
        bufferBuilder.method_22918(matrices.method_23760().method_23761(), f4, f5, f6);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void renderLine(class_4587 matrices, Color color, class_243 start, class_243 end) {
        matrices.method_22903();
        Matrix4f s = matrices.method_23760().method_23761();
        if (ClickGUI.antiAliasing.getValue()) {
            GL11.glEnable((int)32925);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
        }
        GL11.glDepthFunc((int)519);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        RenderUtils.genericAABBRender(class_293.class_5596.field_29344, class_290.field_1576, class_757::method_34540, s, start, end.method_1020(start), color, (buffer, x, y, z, x1, y1, z1, red, green, blue, alpha, matrix) -> {
            buffer.method_22918(matrix, x, y, z).method_22915(red, green, blue, alpha);
            buffer.method_22918(matrix, x1, y1, z1).method_22915(red, green, blue, alpha);
        });
        GL11.glDepthFunc((int)515);
        RenderSystem.disableBlend();
        if (ClickGUI.antiAliasing.getValue()) {
            GL11.glDisable((int)2848);
            GL11.glDisable((int)32925);
        }
        matrices.method_22909();
    }

    private static void genericAABBRender(class_293.class_5596 mode, class_293 format, Supplier<class_5944> shader, Matrix4f stack, class_243 start, class_243 dimensions, Color color, RenderAction action) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        class_243 end = start.method_1019(dimensions);
        float x1 = (float)start.field_1352;
        float y1 = (float)start.field_1351;
        float z1 = (float)start.field_1350;
        float x2 = (float)end.field_1352;
        float y2 = (float)end.field_1351;
        float z2 = (float)end.field_1350;
        RenderUtils.useBuffer(mode, format, shader, bufferBuilder -> action.run((class_287)bufferBuilder, x1, y1, z1, x2, y2, z2, red, green, blue, alpha, stack));
    }

    private static void useBuffer(class_293.class_5596 mode, class_293 format, Supplier<class_5944> shader, Consumer<class_287> runner) {
        class_289 t = class_289.method_1348();
        class_287 bb = t.method_60827(mode, format);
        runner.accept(bb);
        RenderUtils.setup();
        RenderSystem.setShader(shader);
        class_286.method_43433((class_9801)bb.method_60800());
        RenderUtils.cleanup();
    }

    public static void renderBlurredBox(class_4587 matrices, float x, float y, float width, float height, float blurRadius, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (int i = 0; i < 3; ++i) {
            float spread = blurRadius * 0.5f * (float)(i + 1);
            Color blurColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / (i + 2));
            RenderUtils.renderRoundedQuad(matrices, blurColor, x - spread, y - spread, x + width + spread, y + height + spread, 8.0, 8.0, 8.0, 8.0, 15.0);
        }
        RenderSystem.disableBlend();
    }

    public static void renderDropShadow(class_4587 matrices, float x, float y, float width, float height, float shadowRadius, Color shadowColor) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] alphaSteps = new float[]{0.32f, 0.24f, 0.16f, 0.08f, 0.04f};
        float[] spreadSteps = new float[]{0.6f, 1.2f, 1.8f, 2.4f, 3.0f};
        for (int i = 0; i < 5; ++i) {
            float spread = shadowRadius * spreadSteps[i];
            Color currentColor = new Color(0, 0, 0, (int)(160.0f * alphaSteps[i]));
            RenderUtils.renderRoundedQuad(matrices, currentColor, x + spread * 0.25f, y + spread * 0.75f, x + width + spread * 0.25f, y + height + spread * 0.75f, 8.0, 8.0, 8.0, 8.0, 15.0);
        }
        RenderSystem.disableBlend();
    }

    public static void renderBlurredBackground(class_4587 matrices, float x, float y, float width, float height, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] alphaSteps = new float[]{0.6f, 0.4f, 0.2f, 0.1f};
        float[] spreadSteps = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
        Color bgColor = new Color(0, 0, 0, 180);
        RenderUtils.renderRoundedQuad(matrices, bgColor, x, y, x + width, y + height, 8.0, 8.0, 8.0, 8.0, 15.0);
        for (int i = 0; i < 4; ++i) {
            float spread = spreadSteps[i];
            Color blurColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(80.0f * alphaSteps[i]));
            RenderUtils.renderRoundedQuad(matrices, blurColor, x - spread, y - spread, x + width + spread, y + height + spread, 8.0, 8.0, 8.0, 8.0, 15.0);
        }
        RenderSystem.disableBlend();
    }

    static {
        rendering3D = true;
    }

    static interface RenderAction {
        public void run(class_287 var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, Matrix4f var12);
    }
}
