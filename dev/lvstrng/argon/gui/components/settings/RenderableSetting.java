package dev.lvstrng.argon.gui.components.settings;

import dev.lvstrng.argon.gui.components.ModuleButton;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import java.awt.Color;
import net.minecraft.class_310;
import net.minecraft.class_332;

public abstract class RenderableSetting {
    public class_310 mc = class_310.method_1551();
    public ModuleButton parent;
    public Setting<?> setting;
    public int offset;
    public Color currentColor;
    public boolean mouseOver;
    int x;
    int y;
    int width;
    int height;

    public RenderableSetting(ModuleButton parent, Setting<?> setting, int offset) {
        this.parent = parent;
        this.setting = setting;
        this.offset = offset;
        this.x = this.parentX();
        this.y = this.parentY() + this.parentOffset() + offset;
        this.width = this.parentX() + this.parentWidth();
        this.height = this.parentY() + this.parentOffset() + offset + this.parentHeight();
    }

    public int parentX() {
        return this.parent.parent.getX();
    }

    public int parentY() {
        return this.parent.parent.getY();
    }

    public int parentWidth() {
        return this.parent.parent.getWidth();
    }

    public int parentHeight() {
        return this.parent.parent.getHeight();
    }

    public int parentOffset() {
        return this.parent.offset;
    }

    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.updateMouseOver(mouseX, mouseY);
        this.x = this.parentX();
        this.y = this.parentY() + this.parentOffset() + this.offset;
        this.width = this.parentX() + this.parentWidth();
        this.height = this.parentY() + this.parentOffset() + this.offset + this.parentHeight();
        context.method_25294(this.x, this.y, this.width, this.height, this.currentColor.getRGB());
    }

    private void updateMouseOver(double mouseX, double mouseY) {
        this.mouseOver = this.isHovered(mouseX, mouseY);
    }

    public void renderDescription(class_332 context, int mouseX, int mouseY, float delta) {
        if (this.isHovered(mouseX, mouseY) && this.setting.getDescription() != null && !this.parent.parent.dragging) {
            CharSequence chars = this.setting.getDescription();
            int tw = TextRenderer.getWidth(chars);
            int parentCenter = this.mc.method_22683().method_4480() / 2;
            int textCenter = parentCenter - tw / 2;
            RenderUtils.renderRoundedQuad(context.method_51448(), new Color(100, 100, 100, 100), textCenter - 5, this.mc.method_22683().method_4507() / 2 + 294, textCenter + tw + 5, this.mc.method_22683().method_4507() / 2 + 318, 3.0, 10.0);
            TextRenderer.drawString(chars, context, textCenter, this.mc.method_22683().method_4507() / 2 + 300, Color.WHITE.getRGB());
        }
    }

    public void onGuiClose() {
        this.currentColor = null;
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > (double)this.parentX() && mouseX < (double)(this.parentX() + this.parentWidth()) && mouseY > (double)(this.offset + this.parentOffset() + this.parentY()) && mouseY < (double)(this.offset + this.parentOffset() + this.parentY() + this.parentHeight());
    }

    public void onUpdate() {
        this.currentColor = this.currentColor == null ? new Color(0, 0, 0, 0) : new Color(0, 0, 0, this.currentColor.getAlpha());
        int toAlpha = 120;
        if (this.currentColor.getAlpha() != toAlpha) {
            this.currentColor = ColorUtils.smoothAlphaTransition(0.05f, toAlpha, this.currentColor);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
    }
}
