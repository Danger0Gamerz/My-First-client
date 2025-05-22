package dev.lvstrng.argon.font;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_9801;
import org.lwjgl.BufferUtils;

public final class GlyphPage {
    private int imgSize;
    private int maxFontHeight = -1;
    private final Font font;
    private final boolean antiAliasing;
    private final boolean fractionalMetrics;
    private final HashMap<Character, Glyph> glyphCharacterMap = new HashMap();
    private BufferedImage bufferedImage;
    private class_1044 loadedTexture;

    public GlyphPage(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars) {
        double maxWidth = -1.0;
        double maxHeight = -1.0;
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, this.antiAliasing, this.fractionalMetrics);
        for (char ch : chars) {
            Rectangle2D bounds = this.font.getStringBounds(Character.toString(ch), fontRenderContext);
            if (maxWidth < bounds.getWidth()) {
                maxWidth = bounds.getWidth();
            }
            if (!(maxHeight < bounds.getHeight())) continue;
            maxHeight = bounds.getHeight();
        }
        this.imgSize = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt((maxWidth += 2.0) * maxWidth * (double)chars.length) / maxWidth), Math.ceil(Math.sqrt((maxHeight += 2.0) * maxHeight * (double)chars.length) / maxHeight)) * Math.max(maxWidth, maxHeight)) + 1;
        this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 2);
        Graphics2D g = this.bufferedImage.createGraphics();
        g.setFont(this.font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, this.imgSize, this.imgSize);
        g.setColor(Color.white);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        FontMetrics fontMetrics = g.getFontMetrics();
        int currentCharHeight = 0;
        int posX = 0;
        int posY = 1;
        for (char ch : chars) {
            Glyph glyph = new Glyph();
            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);
            glyph.width = bounds.getBounds().width + 8;
            glyph.height = bounds.getBounds().height;
            if (posX + glyph.width >= this.imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }
            glyph.x = posX;
            glyph.y = posY;
            if (glyph.height > this.maxFontHeight) {
                this.maxFontHeight = glyph.height;
            }
            if (glyph.height > currentCharHeight) {
                currentCharHeight = glyph.height;
            }
            g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());
            posX += glyph.width;
            this.glyphCharacterMap.put(Character.valueOf(ch), glyph);
        }
    }

    public void setupTexture() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage)this.bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            ByteBuffer data = BufferUtils.createByteBuffer((int)bytes.length).put(bytes);
            data.flip();
            this.loadedTexture = new class_1043(class_1011.method_4324((ByteBuffer)data));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bindTexture() {
        RenderSystem.setShaderTexture((int)0, (int)this.loadedTexture.method_4624());
    }

    public void unbindTexture() {
        RenderSystem.setShaderTexture((int)0, (int)0);
    }

    public float drawChar(class_4587 stack, char ch, float x, float y, float red, float blue, float green, float alpha) {
        Glyph glyph = this.glyphCharacterMap.get(Character.valueOf(ch));
        if (glyph == null) {
            return 0.0f;
        }
        float pageX = (float)glyph.x / (float)this.imgSize;
        float pageY = (float)glyph.y / (float)this.imgSize;
        float pageWidth = (float)glyph.width / (float)this.imgSize;
        float pageHeight = (float)glyph.height / (float)this.imgSize;
        float width = glyph.width;
        float height = glyph.height;
        RenderSystem.setShader(class_757::method_34543);
        this.bindTexture();
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1575);
        bufferBuilder.method_22918(stack.method_23760().method_23761(), x, y + height, 0.0f).method_22915(red, green, blue, alpha).method_22913(pageX, pageY + pageHeight);
        bufferBuilder.method_22918(stack.method_23760().method_23761(), x + width, y + height, 0.0f).method_22915(red, green, blue, alpha).method_22913(pageX + pageWidth, pageY + pageHeight);
        bufferBuilder.method_22918(stack.method_23760().method_23761(), x + width, y, 0.0f).method_22915(red, green, blue, alpha).method_22913(pageX + pageWidth, pageY);
        bufferBuilder.method_22918(stack.method_23760().method_23761(), x, y, 0.0f).method_22915(red, green, blue, alpha).method_22913(pageX, pageY);
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        this.unbindTexture();
        return width - 8.0f;
    }

    public float getWidth(char ch) {
        return this.glyphCharacterMap.get((Object)Character.valueOf((char)ch)).width;
    }

    public boolean isAntiAliasingEnabled() {
        return this.antiAliasing;
    }

    public boolean isFractionalMetricsEnabled() {
        return this.fractionalMetrics;
    }

    public int getMaxFontHeight() {
        return this.maxFontHeight;
    }

    static class Glyph {
        private int x;
        private int y;
        private int width;
        private int height;

        Glyph(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Glyph() {
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
