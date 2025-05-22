package dev.lvstrng.argon.gui.components.settings;

import dev.lvstrng.argon.gui.components.ModuleButton;
import dev.lvstrng.argon.gui.components.settings.RenderableSetting;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;

public final class MinMaxSlider
extends RenderableSetting {
    public boolean draggingMin;
    public boolean draggingMax;
    public double offsetMinX;
    public double offsetMaxX;
    public double lerpedOffsetMinX = this.parentX();
    public double lerpedOffsetMaxX = this.parentX() + this.parentWidth();
    public MinMaxSetting setting;
    public Color currentColor1;
    public Color currentColor2;
    private Color currentAlpha;

    public MinMaxSlider(ModuleButton parent, Setting<?> setting, int offset) {
        super(parent, setting, offset);
        this.setting = (MinMaxSetting)setting;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        class_4587 matrices = context.method_51448();
        this.offsetMinX = (this.setting.getMinValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * (double)this.parentWidth();
        this.offsetMaxX = (this.setting.getMaxValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * (double)this.parentWidth();
        this.lerpedOffsetMinX = MathUtils.goodLerp((float)(0.5 * (double)delta), this.lerpedOffsetMinX, this.offsetMinX);
        this.lerpedOffsetMaxX = MathUtils.goodLerp((float)(0.5 * (double)delta), this.lerpedOffsetMaxX, this.offsetMaxX);
        String str = String.valueOf(this.setting.getName()) + ": " + String.valueOf(this.setting.getMinValue() == this.setting.getMaxValue() ? Double.valueOf(this.setting.getMinValue()) : this.setting.getMinValue() + " - " + this.setting.getMaxValue());
        context.method_25296((int)((double)this.parentX() + this.lerpedOffsetMinX), this.parentY() + this.offset + this.parentOffset() + 25, (int)((double)this.parentX() + this.lerpedOffsetMinX + this.getLength()), this.parentY() + this.offset + this.parentOffset() + this.parentHeight(), this.currentColor1.getRGB(), this.currentColor2.getRGB());
        float scalable = 0.8f;
        matrices.method_22903();
        matrices.method_22905(scalable, scalable, 1.0f);
        TextRenderer.drawString(str, context, (int)((float)(this.parentX() + 5) / scalable), (int)((float)(this.parentY() + this.parentOffset() + this.offset + 9) / scalable), new Color(245, 245, 245, 255).getRGB());
        matrices.method_22905(1.0f, 1.0f, 1.0f);
        matrices.method_22909();
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
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.isHoveredMin(mouseX, mouseY) || this.isMouseInMin(mouseX, mouseY)) {
                this.draggingMin = true;
                this.slideMin(mouseX);
            } else if (this.isHoveredMax(mouseX, mouseY) || this.isMouseInMax(mouseX, mouseY)) {
                this.draggingMax = true;
                this.slideMax(mouseX);
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.mouseOver && keyCode == 259) {
            this.setting.setMaxValue(this.setting.getOriginalMaxValue());
            this.setting.setMinValue(this.setting.getOriginalMinValue());
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isHoveredMin(double mouseX, double mouseY) {
        return this.isHovered(mouseX, mouseY) && mouseX > (double)this.parentX() + this.offsetMinX - 4.0 && mouseX < (double)this.parentX() + this.offsetMinX + 4.0;
    }

    public boolean isHoveredMax(double mouseX, double mouseY) {
        return this.isHovered(mouseX, mouseY) && mouseX > (double)this.parentX() + this.offsetMaxX - 4.0 && mouseX < (double)this.parentX() + this.offsetMaxX + 4.0;
    }

    public double getLength() {
        return this.lerpedOffsetMaxX - this.lerpedOffsetMinX;
    }

    public boolean isMouseInMin(double mouseX, double mouseY) {
        return this.isHovered(mouseX, mouseY) && (mouseX <= (double)this.parentX() + this.offsetMinX || mouseX < (double)this.parentX() + this.offsetMinX + this.getLength() / 2.0);
    }

    public boolean isMouseInMax(double mouseX, double mouseY) {
        return this.isHovered(mouseX, mouseY) && (mouseX > (double)this.parentX() + this.offsetMaxX || mouseX > (double)this.parentX() + this.offsetMinX + this.getLength() / 2.0);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (this.draggingMin) {
                this.draggingMin = false;
            }
            if (this.draggingMax) {
                this.draggingMax = false;
            }
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.draggingMin) {
            this.slideMin(mouseX);
        }
        if (this.draggingMax && !this.draggingMin) {
            this.slideMax(mouseX);
        }
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void onGuiClose() {
        this.currentColor1 = null;
        this.currentColor2 = null;
        super.onGuiClose();
    }

    private void slideMin(double mouseX) {
        double a = mouseX - (double)this.parentX();
        double b = class_3532.method_15350((double)(a / (double)this.parentWidth()), (double)0.0, (double)1.0);
        this.setting.setMinValue(MathUtils.roundToDecimal(b * (this.setting.getMax() - this.setting.getMin()) + this.setting.getMin(), this.setting.getIncrement()));
    }

    private void slideMax(double mouseX) {
        double a = mouseX - (double)this.parentX();
        double b = class_3532.method_15350((double)(a / (double)this.parentWidth()), (double)0.0, (double)1.0);
        this.setting.setMaxValue(MathUtils.roundToDecimal(b * (this.setting.getMax() - this.setting.getMin()) + this.setting.getMin(), this.setting.getIncrement()));
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
        if (this.draggingMin) {
            this.draggingMax = false;
        }
        if (this.setting.getMinValue() > this.setting.getMaxValue()) {
            this.setting.setMaxValue(this.setting.getMinValue());
        }
        if (this.setting.getMaxValue() < this.setting.getMinValue()) {
            this.setting.setMinValue(this.setting.getMaxValue() - this.setting.getIncrement());
        }
        super.onUpdate();
    }
}
