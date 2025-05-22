package dev.lvstrng.argon.utils;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.font.Fonts;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public final class TextRenderer {
    public static void drawString(CharSequence string, class_332 context, int x, int y, int color) {
        boolean custom = ClickGUI.customFont.getValue();
        if (custom) {
            Fonts.QUICKSAND.drawString(context.method_51448(), string, x, y - 8, color);
        } else {
            TextRenderer.drawMinecraftText(string, context, x, y, color);
        }
    }

    public static int getWidth(CharSequence string) {
        boolean custom = ClickGUI.customFont.getValue();
        if (custom) {
            return Fonts.QUICKSAND.getStringWidth(string);
        }
        return Argon.mc.field_1772.method_1727(string.toString()) * 2;
    }

    public static void drawCenteredString(CharSequence string, class_332 context, int x, int y, int color) {
        boolean custom = ClickGUI.customFont.getValue();
        if (custom) {
            Fonts.QUICKSAND.drawString(context.method_51448(), string, x - Fonts.QUICKSAND.getStringWidth(string) / 2, y - 8, color);
        } else {
            TextRenderer.drawCenteredMinecraftText(string, context, x, y, color);
        }
    }

    public static void drawLargeString(CharSequence string, class_332 context, int x, int y, int color) {
        boolean custom = ClickGUI.customFont.getValue();
        if (custom) {
            class_4587 matrices = context.method_51448();
            matrices.method_22903();
            matrices.method_22905(1.4f, 1.4f, 1.4f);
            Fonts.QUICKSAND.drawString(context.method_51448(), string, x, y - 8, color);
            matrices.method_22905(1.0f, 1.0f, 1.0f);
            matrices.method_22909();
        } else {
            TextRenderer.drawLargerMinecraftText(string, context, x, y, color);
        }
    }

    public static void drawMinecraftText(CharSequence string, class_332 context, int x, int y, int color) {
        class_4587 matrices = context.method_51448();
        matrices.method_22903();
        matrices.method_22905(2.0f, 2.0f, 2.0f);
        context.method_51433(Argon.mc.field_1772, string.toString(), x / 2, y / 2, color, false);
        matrices.method_22905(1.0f, 1.0f, 1.0f);
        matrices.method_22909();
    }

    public static void drawLargerMinecraftText(CharSequence string, class_332 context, int x, int y, int color) {
        class_4587 matrices = context.method_51448();
        matrices.method_22903();
        matrices.method_22905(3.0f, 3.0f, 3.0f);
        context.method_51433(Argon.mc.field_1772, (String)string, x / 3, y / 3, color, false);
        matrices.method_22905(1.0f, 1.0f, 1.0f);
        matrices.method_22909();
    }

    public static void drawCenteredMinecraftText(CharSequence string, class_332 context, int x, int y, int color) {
        class_4587 matrices = context.method_51448();
        matrices.method_22903();
        matrices.method_22905(2.0f, 2.0f, 2.0f);
        context.method_51433(Argon.mc.field_1772, (String)string, x / 2 - Argon.mc.field_1772.method_1727((String)string) / 2, y / 2, color, false);
        matrices.method_22905(1.0f, 1.0f, 1.0f);
        matrices.method_22909();
    }
}
