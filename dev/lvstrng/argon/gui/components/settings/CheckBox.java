package dev.lvstrng.argon.gui.components.settings;

import dev.lvstrng.argon.gui.components.ModuleButton;
import dev.lvstrng.argon.gui.components.settings.RenderableSetting;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import net.minecraft.class_332;

public final class CheckBox
extends RenderableSetting {
    private final BooleanSetting setting;
    private Color currentAlpha;

    public CheckBox(ModuleButton parent, Setting<?> setting, int offset) {
        super(parent, setting, offset);
        this.setting = (BooleanSetting)setting;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int nameOffset = this.parentX() + 31;
        CharSequence chars = this.setting.getName();
        TextRenderer.drawString(chars, context, nameOffset, this.parentY() + this.parentOffset() + this.offset + 9, new Color(245, 245, 245, 255).getRGB());
        context.method_25296(this.parentX() + 5, this.parentY() + this.parentOffset() + this.offset + 5, this.parentX() + 25, this.parentY() + this.parentOffset() + this.offset + this.parentHeight() - 5, Utils.getMainColor(255, this.parent.settings.indexOf(this)).getRGB(), Utils.getMainColor(255, this.parent.settings.indexOf(this) + 1).getRGB());
        context.method_25294(this.parentX() + 7, this.parentY() + this.parentOffset() + this.offset + 7, this.parentX() + 23, this.parentY() + this.parentOffset() + this.offset + this.parentHeight() - 7, Color.darkGray.getRGB());
        context.method_25296(this.parentX() + 9, this.parentY() + this.parentOffset() + this.offset + 9, this.parentX() + 21, this.parentY() + this.parentOffset() + this.offset + this.parentHeight() - 9, this.setting.getValue() ? Utils.getMainColor(255, this.parent.settings.indexOf(this)).getRGB() : Color.darkGray.getRGB(), this.setting.getValue() ? Utils.getMainColor(255, this.parent.settings.indexOf(this) + 1).getRGB() : Color.darkGray.getRGB());
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
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.mouseOver && this.parent.extended && keyCode == 259) {
            this.setting.setValue(this.setting.getOriginalValue());
        }
        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.setting.toggle();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}
