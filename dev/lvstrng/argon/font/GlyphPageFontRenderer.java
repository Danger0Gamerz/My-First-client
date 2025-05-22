package dev.lvstrng.argon.font;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.lvstrng.argon.font.GlyphPage;
import dev.lvstrng.argon.utils.EncryptedString;
import java.awt.Font;
import java.util.Random;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_9801;

public final class GlyphPageFontRenderer {
    public Random fontRandom = new Random();
    private float posX;
    private float posY;
    private final int[] colorCode = new int[32];
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    private final GlyphPage regularGlyphPage;
    private final GlyphPage boldGlyphPage;
    private final GlyphPage italicGlyphPage;
    private final GlyphPage boldItalicGlyphPage;

    public GlyphPageFontRenderer(GlyphPage regularGlyphPage, GlyphPage boldGlyphPage, GlyphPage italicGlyphPage, GlyphPage boldItalicGlyphPage) {
        this.regularGlyphPage = regularGlyphPage;
        this.boldGlyphPage = boldGlyphPage;
        this.italicGlyphPage = italicGlyphPage;
        this.boldItalicGlyphPage = boldItalicGlyphPage;
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
    }

    public static GlyphPageFontRenderer create(CharSequence fontName, int size, boolean bold, boolean italic, boolean boldItalic) {
        char[] chars = new char[256];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = (char)i;
        }
        GlyphPage regularPage = new GlyphPage(new Font(fontName.toString(), 0, size), true, true);
        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();
        GlyphPage boldPage = regularPage;
        GlyphPage italicPage = regularPage;
        GlyphPage boldItalicPage = regularPage;
        if (bold) {
            boldPage = new GlyphPage(new Font(fontName.toString(), 1, size), true, true);
            boldPage.generateGlyphPage(chars);
            boldPage.setupTexture();
        }
        if (italic) {
            italicPage = new GlyphPage(new Font(fontName.toString(), 2, size), true, true);
            italicPage.generateGlyphPage(chars);
            italicPage.setupTexture();
        }
        if (boldItalic) {
            boldItalicPage = new GlyphPage(new Font(fontName.toString(), 3, size), true, true);
            boldItalicPage.generateGlyphPage(chars);
            boldItalicPage.setupTexture();
        }
        return new GlyphPageFontRenderer(regularPage, boldPage, italicPage, boldItalicPage);
    }

    public static GlyphPageFontRenderer createFromID(CharSequence id, int size, boolean bold, boolean italic, boolean boldItalic) {
        char[] chars = new char[256];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = (char)i;
        }
        Font font = null;
        try {
            font = Font.createFont(0, GlyphPageFontRenderer.class.getResourceAsStream(id.toString())).deriveFont(0, size);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        GlyphPage regularPage = new GlyphPage(font, true, true);
        regularPage.generateGlyphPage(chars);
        regularPage.setupTexture();
        GlyphPage boldPage = regularPage;
        GlyphPage italicPage = regularPage;
        GlyphPage boldItalicPage = regularPage;
        try {
            if (bold) {
                boldPage = new GlyphPage(Font.createFont(0, GlyphPageFontRenderer.class.getResourceAsStream(id.toString())).deriveFont(1, size), true, true);
                boldPage.generateGlyphPage(chars);
                boldPage.setupTexture();
            }
            if (italic) {
                italicPage = new GlyphPage(Font.createFont(0, GlyphPageFontRenderer.class.getResourceAsStream(id.toString())).deriveFont(2, size), true, true);
                italicPage.generateGlyphPage(chars);
                italicPage.setupTexture();
            }
            if (boldItalic) {
                boldItalicPage = new GlyphPage(Font.createFont(0, GlyphPageFontRenderer.class.getResourceAsStream(id.toString())).deriveFont(3, size), true, true);
                boldItalicPage.generateGlyphPage(chars);
                boldItalicPage.setupTexture();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new GlyphPageFontRenderer(regularPage, boldPage, italicPage, boldItalicPage);
    }

    public int drawStringWithShadow(class_4587 matrices, CharSequence text, float x, float y, int color) {
        return this.drawString(matrices, text, x, y, color, true);
    }

    public int drawStringWithShadow(class_4587 matrices, CharSequence text, double x, double y, int color) {
        return this.drawString(matrices, text, (float)x, (float)y, color, true);
    }

    public int drawString(class_4587 matrices, CharSequence text, float x, float y, int color) {
        return this.drawString(matrices, text, x, y, color, false);
    }

    public int drawString(class_4587 matrices, CharSequence text, double x, double y, int color) {
        return this.drawString(matrices, text, (float)x, (float)y, color, false);
    }

    public int drawCenteredString(class_4587 matrices, CharSequence text, double x, double y, float scale, int color) {
        return this.drawString(matrices, text, (float)x - (float)(this.getStringWidth(text) / 2), (float)y, scale, color, false);
    }

    public int drawCenteredString(class_4587 matrices, CharSequence text, double x, double y, int color) {
        return this.drawString(matrices, text, (float)x - (float)(this.getStringWidth(text) / 2), (float)y, color, false);
    }

    public int drawCenteredStringWidthShadow(class_4587 matrices, CharSequence text, double x, double y, int color) {
        return this.drawString(matrices, text, (float)x - (float)(this.getStringWidth(text) / 2), (float)y, color, true);
    }

    public int drawString(class_4587 matrices, CharSequence text, float x, float y, float scale, int color, boolean dropShadow) {
        int i;
        this.resetStyles();
        if (dropShadow) {
            i = this.renderString(matrices, text, x + 1.0f, y + 1.0f, scale, color, true);
            i = Math.max(i, this.renderString(matrices, text, x, y, scale, color, false));
        } else {
            i = this.renderString(matrices, text, x, y, scale, color, false);
        }
        return i;
    }

    public int drawString(class_4587 matrices, CharSequence text, float x, float y, int color, boolean dropShadow) {
        int i;
        this.resetStyles();
        if (dropShadow) {
            i = this.renderString(matrices, text, x + 1.0f, y + 1.0f, color, true);
            i = Math.max(i, this.renderString(matrices, text, x, y, color, false));
        } else {
            i = this.renderString(matrices, text, x, y, color, false);
        }
        return i;
    }

    private int renderString(class_4587 matrices, CharSequence text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        this.posX = x * 2.0f;
        this.posY = y * 2.0f;
        this.renderStringAtPos(matrices, text, dropShadow, color);
        return (int)(this.posX / 4.0f);
    }

    private int renderString(class_4587 matrices, CharSequence text, float x, float y, float scale, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        this.posX = x * 2.0f;
        this.posY = y * 2.0f;
        this.renderStringAtPos(matrices, text, scale, dropShadow, color);
        return (int)(this.posX / 4.0f);
    }

    private void renderStringAtPos(class_4587 matrices, CharSequence text, boolean shadow, int color) {
        GlyphPage glyphPage = this.getCurrentGlyphPage();
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        matrices.method_22903();
        matrices.method_22905(0.5f, 0.5f, 0.5f);
        GlStateManager._enableBlend();
        GlStateManager._blendFunc((int)770, (int)771);
        glyphPage.bindTexture();
        GlStateManager._texParameter((int)3553, (int)10240, (int)9729);
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            if (c0 == '\u00a7' && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(text.charAt(i + 1)));
                if (i1 < 16) {
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i1 < 0) {
                        i1 = 15;
                    }
                    if (shadow) {
                        i1 += 16;
                    }
                    int j1 = this.colorCode[i1];
                    g = (float)(j1 >> 16 & 0xFF) / 255.0f;
                    h = (float)(j1 >> 8 & 0xFF) / 255.0f;
                    k = (float)(j1 & 0xFF) / 255.0f;
                } else if (i1 != 16) {
                    if (i1 == 17) {
                        this.boldStyle = true;
                    } else if (i1 == 18) {
                        this.strikethroughStyle = true;
                    } else if (i1 == 19) {
                        this.underlineStyle = true;
                    } else if (i1 == 20) {
                        this.italicStyle = true;
                    } else {
                        this.boldStyle = false;
                        this.strikethroughStyle = false;
                        this.underlineStyle = false;
                        this.italicStyle = false;
                    }
                }
                ++i;
                continue;
            }
            glyphPage = this.getCurrentGlyphPage();
            glyphPage.bindTexture();
            float f = glyphPage.drawChar(matrices, c0, this.posX, this.posY, g, k, h, alpha);
            this.doDraw(f, glyphPage);
        }
        glyphPage.unbindTexture();
        matrices.method_22909();
    }

    private void renderStringAtPos(class_4587 matrices, CharSequence text, float scale, boolean shadow, int color) {
        GlyphPage glyphPage = this.getCurrentGlyphPage();
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float g = (float)(color >> 16 & 0xFF) / 255.0f;
        float h = (float)(color >> 8 & 0xFF) / 255.0f;
        float k = (float)(color & 0xFF) / 255.0f;
        matrices.method_22903();
        matrices.method_22905(scale, scale, scale);
        GlStateManager._enableBlend();
        GlStateManager._blendFunc((int)770, (int)771);
        glyphPage.bindTexture();
        GlStateManager._texParameter((int)3553, (int)10240, (int)9729);
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            if (c0 == '\u00a7' && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(text.charAt(i + 1)));
                if (i1 < 16) {
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i1 < 0) {
                        i1 = 15;
                    }
                    if (shadow) {
                        i1 += 16;
                    }
                    int j1 = this.colorCode[i1];
                    g = (float)(j1 >> 16 & 0xFF) / 255.0f;
                    h = (float)(j1 >> 8 & 0xFF) / 255.0f;
                    k = (float)(j1 & 0xFF) / 255.0f;
                } else if (i1 != 16) {
                    if (i1 == 17) {
                        this.boldStyle = true;
                    } else if (i1 == 18) {
                        this.strikethroughStyle = true;
                    } else if (i1 == 19) {
                        this.underlineStyle = true;
                    } else if (i1 == 20) {
                        this.italicStyle = true;
                    } else {
                        this.boldStyle = false;
                        this.strikethroughStyle = false;
                        this.underlineStyle = false;
                        this.italicStyle = false;
                    }
                }
                ++i;
                continue;
            }
            glyphPage = this.getCurrentGlyphPage();
            glyphPage.bindTexture();
            float f = glyphPage.drawChar(matrices, c0, this.posX, this.posY, g, k, h, alpha);
            this.doDraw(f, glyphPage);
        }
        glyphPage.unbindTexture();
        matrices.method_22909();
    }

    private void doDraw(float f, GlyphPage glyphPage) {
        class_287 bufferBuilder;
        if (this.strikethroughStyle) {
            bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
            bufferBuilder.method_22912(this.posX, this.posY + (float)(glyphPage.getMaxFontHeight() / 2), 0.0f);
            bufferBuilder.method_22912(this.posX + f, this.posY + (float)(glyphPage.getMaxFontHeight() / 2), 0.0f);
            bufferBuilder.method_22912(this.posX + f, this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0f, 0.0f);
            bufferBuilder.method_22912(this.posX, this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0f, 0.0f);
            class_286.method_43433((class_9801)bufferBuilder.method_60800());
        }
        if (this.underlineStyle) {
            bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1592);
            int l = this.underlineStyle ? -1 : 0;
            bufferBuilder.method_22912(this.posX + (float)l, this.posY + (float)glyphPage.getMaxFontHeight(), 0.0f);
            bufferBuilder.method_22912(this.posX + f, this.posY + (float)glyphPage.getMaxFontHeight(), 0.0f);
            bufferBuilder.method_22912(this.posX + f, this.posY + (float)glyphPage.getMaxFontHeight() - 1.0f, 0.0f);
            bufferBuilder.method_22912(this.posX + (float)l, this.posY + (float)glyphPage.getMaxFontHeight() - 1.0f, 0.0f);
            class_286.method_43433((class_9801)bufferBuilder.method_60800());
        }
        this.posX += f;
    }

    private GlyphPage getCurrentGlyphPage() {
        if (this.boldStyle && this.italicStyle) {
            return this.boldItalicGlyphPage;
        }
        if (this.boldStyle) {
            return this.boldGlyphPage;
        }
        if (this.italicStyle) {
            return this.italicGlyphPage;
        }
        return this.regularGlyphPage;
    }

    private void resetStyles() {
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    public int getFontHeight() {
        return this.regularGlyphPage.getMaxFontHeight() / 2;
    }

    public int getStringWidth(CharSequence text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        int size = text.length();
        boolean on = false;
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == '\ufffd') {
                on = true;
                continue;
            }
            if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                } else if (colorIndex == 17) {
                    this.boldStyle = true;
                } else if (colorIndex == 20) {
                    this.italicStyle = true;
                } else if (colorIndex == 21) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                ++i;
                on = false;
                continue;
            }
            if (on) {
                --i;
            }
            character = text.charAt(i);
            GlyphPage currentPage = this.getCurrentGlyphPage();
            width = (int)((float)width + (currentPage.getWidth(character) - 8.0f));
        }
        return width / 2;
    }

    public CharSequence trimStringToWidth(CharSequence text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public CharSequence trimStringToWidth(CharSequence text, int maxWidth, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        boolean on = false;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        int width = 0;
        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);
            if (character == '\ufffd') {
                on = true;
            } else if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                } else if (colorIndex == 17) {
                    this.boldStyle = true;
                } else if (colorIndex == 20) {
                    this.italicStyle = true;
                } else if (colorIndex == 21) {
                    this.boldStyle = false;
                    this.italicStyle = false;
                }
                ++i;
                on = false;
            } else {
                if (on) {
                    --i;
                }
                character = text.charAt(i);
                GlyphPage currentPage = this.getCurrentGlyphPage();
                width = (int)((float)width + (currentPage.getWidth(character) - 8.0f) / 2.0f);
            }
            if (i > width) break;
            if (reverse) {
                stringbuilder.insert(0, character);
                continue;
            }
            stringbuilder.append(character);
        }
        return EncryptedString.of(stringbuilder.toString());
    }
}
