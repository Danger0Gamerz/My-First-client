package dev.lvstrng.argon.gui.components.settings;

import dev.lvstrng.argon.gui.components.ModuleButton;
import dev.lvstrng.argon.gui.components.settings.RenderableSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_3532;

public final class Slider
extends RenderableSetting {
    public boolean dragging;
    public double offsetX;
    public double lerpedOffsetX = 0.0;
    private final NumberSetting setting;
    public Color currentColor1;
    public Color currentColor2;
    private Color currentAlpha;

    public Slider(ModuleButton parent, Setting<?> setting, int offset) {
        super(parent, setting, offset);
        this.setting = (NumberSetting)setting;
    }

    @Override
    public void onUpdate() {
        Color clr = Utils.getMainColor(0, this.parent.settings.indexOf(this)).darker();
        Color clr2 = Utils.getMainColor(0, this.parent.settings.indexOf(this) + 1).darker();
        this.currentColor1 = this.currentColor1 == null ? new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), 0) : new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), this.currentColor1.getAlpha());
        this.currentColor2 = this.currentColor2 == null ? new Color(clr2.getRed(), clr2.getGreen(), clr2.getBlue(), 0) : new Color(clr2.getRed(), clr2.getGreen(), clr2.getBlue(), this.currentColor2.getAlpha());
        int toAlpha = 255;
        if (this.currentColor1.getAlpha() != toAlpha) {
            this.currentColor1 = ColorUtils.smoothAlphaTransition(0.05f, toAlpha, this.currentColor1);
        }
        if (this.currentColor2.getAlpha() != toAlpha) {
            this.currentColor2 = ColorUtils.smoothAlphaTransition(0.05f, toAlpha, this.currentColor2);
        }
        super.onUpdate();
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.offsetX = (this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * (double)this.parentWidth();
        this.lerpedOffsetX = MathUtils.goodLerp((float)(0.5 * (double)delta), this.lerpedOffsetX, this.offsetX);
        context.method_25296(this.parentX(), this.parentY() + this.offset + this.parentOffset() + 25, (int)((double)this.parentX() + this.lerpedOffsetX), this.parentY() + this.offset + this.parentOffset() + this.parentHeight(), this.currentColor1.getRGB(), this.currentColor2.getRGB());
        TextRenderer.drawString(String.valueOf(this.setting.getName()) + ": " + this.setting.getValue(), context, this.parentX() + 5, this.parentY() + this.parentOffset() + this.offset + 9, new Color(245, 245, 245, 255).getRGB());
        if (!this.parent.parent.dragging) {
            int toHoverAlpha = this.isHovered(mouseX, mouseY) ? 15 : 0;
            this.currentAlpha = this.currentAlpha == null ? new Color(255, 255, 255, toHoverAlpha) : new Color(255, 255, 255, this.currentAlpha.getAlpha());
            if (this.currentAlpha.getAlpha() != toHoverAlpha) {
                this.currentAlpha = ColorUtils.smoothAlphaTransition(0.05f, toHoverAlpha, this.currentAlpha);
            }
            context.method_25294(this.parentX(), this.parentY() + this.parentOffset() + this.offset, this.parentX() + this.parentWidth(), this.parentY() + this.parentOffset() + this.offset + this.parentHeight(), this.currentAlpha.getRGB());
        }
    }

    @Override
    public void onGuiClose() {
        this.currentColor1 = null;
        this.currentColor2 = null;
        super.onGuiClose();
    }

    private void slide(double mouseX) {
        double a = mouseX - (double)this.parentX();
        double b = class_3532.method_15350((double)(a / (double)this.parentWidth()), (double)0.0, (double)1.0);
        this.setting.setValue(MathUtils.roundToDecimal(b * (this.setting.getMax() - this.setting.getMin()) + this.setting.getMin(), this.setting.getIncrement()));
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.mouseOver && this.parent.extended && keyCode == 259) {
            this.setting.setValue(this.setting.getOriginalValue());
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.dragging = true;
            this.slide(mouseX);
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.dragging && button == 0) {
            this.dragging = false;
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.dragging) {
            this.slide(mouseX);
        }
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
}
