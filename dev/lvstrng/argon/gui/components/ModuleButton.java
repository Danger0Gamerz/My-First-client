package dev.lvstrng.argon.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.gui.Window;
import dev.lvstrng.argon.gui.components.settings.CheckBox;
import dev.lvstrng.argon.gui.components.settings.KeybindBox;
import dev.lvstrng.argon.gui.components.settings.MinMaxSlider;
import dev.lvstrng.argon.gui.components.settings.ModeBox;
import dev.lvstrng.argon.gui.components.settings.RenderableSetting;
import dev.lvstrng.argon.gui.components.settings.Slider;
import dev.lvstrng.argon.gui.components.settings.StringBox;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.KeybindSetting;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.module.setting.StringSetting;
import dev.lvstrng.argon.utils.AnimationUtils;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import net.minecraft.class_332;

public final class ModuleButton {
    public List<RenderableSetting> settings = new ArrayList<RenderableSetting>();
    public Window parent;
    public Module module;
    public int offset;
    public boolean extended;
    public int settingOffset;
    public Color currentColor;
    public Color defaultColor = Color.WHITE;
    public Color currentAlpha;
    public AnimationUtils animation = new AnimationUtils(0.0);

    public ModuleButton(Window parent, Module module, int offset) {
        this.parent = parent;
        this.module = module;
        this.offset = offset;
        this.extended = false;
        this.settingOffset = parent.getHeight();
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                BooleanSetting booleanSetting = (BooleanSetting)setting;
                this.settings.add(new CheckBox(this, booleanSetting, this.settingOffset));
            } else if (setting instanceof NumberSetting) {
                NumberSetting numberSetting = (NumberSetting)setting;
                this.settings.add(new Slider(this, numberSetting, this.settingOffset));
            } else if (setting instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting)setting;
                this.settings.add(new ModeBox(this, modeSetting, this.settingOffset));
            } else if (setting instanceof KeybindSetting) {
                KeybindSetting keybindSetting = (KeybindSetting)setting;
                this.settings.add(new KeybindBox(this, keybindSetting, this.settingOffset));
            } else if (setting instanceof StringSetting) {
                StringSetting stringSetting = (StringSetting)setting;
                this.settings.add(new StringBox(this, stringSetting, this.settingOffset));
            } else if (setting instanceof MinMaxSetting) {
                MinMaxSetting minMaxSetting = (MinMaxSetting)setting;
                this.settings.add(new MinMaxSlider(this, minMaxSetting, this.settingOffset));
            }
            this.settingOffset += parent.getHeight();
        }
    }

    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        Color toColor;
        if (this.parent.getY() + this.offset > class_310.method_1551().method_22683().method_4507()) {
            return;
        }
        for (RenderableSetting renderableSetting : this.settings) {
            renderableSetting.onUpdate();
        }
        this.currentColor = this.currentColor == null ? new Color(0, 0, 0, 0) : new Color(0, 0, 0, this.currentColor.getAlpha());
        int toAlpha = 170;
        this.currentColor = ColorUtils.smoothAlphaTransition(0.05f, toAlpha, this.currentColor);
        Color color = toColor = this.module.isEnabled() ? Utils.getMainColor(255, Argon.INSTANCE.getModuleManager().getModulesInCategory(this.module.getCategory()).indexOf(this.module)) : Color.WHITE;
        if (this.defaultColor != toColor) {
            this.defaultColor = ColorUtils.smoothColorTransition(0.1f, toColor, this.defaultColor);
        }
        if (this.parent.moduleButtons.get(this.parent.moduleButtons.size() - 1) != this) {
            context.method_25294(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.parent.getHeight() + this.offset, this.currentColor.getRGB());
            context.method_25296(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + 2, this.parent.getY() + this.parent.getHeight() + this.offset, Utils.getMainColor(255, Argon.INSTANCE.getModuleManager().getModulesInCategory(this.module.getCategory()).indexOf(this.module)).getRGB(), Utils.getMainColor(255, Argon.INSTANCE.getModuleManager().getModulesInCategory(this.module.getCategory()).indexOf(this.module) + 1).getRGB());
        } else {
            RenderUtils.renderRoundedQuad(context.method_51448(), this.currentColor, this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.parent.getHeight() + this.offset, 0.0, 0.0, 3.0, this.animation.getValue() > 30.0 ? 0.0 : (double)ClickGUI.roundQuads.getValueInt(), 50.0);
            RenderUtils.renderRoundedQuad(context.method_51448(), Utils.getMainColor(255, Argon.INSTANCE.getModuleManager().getModulesInCategory(this.module.getCategory()).indexOf(this.module)), this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + 2, this.parent.getY() + (this.parent.getHeight() - 1) + this.offset, 0.0, 0.0, this.extended ? 0.0 : 2.0, 0.0, 50.0);
        }
        CharSequence nameChars = this.module.getName();
        int totalWidth = TextRenderer.getWidth(nameChars);
        int parentCenterX = this.parent.getX() + this.parent.getWidth() / 2;
        int textCenterX = parentCenterX - totalWidth / 2;
        TextRenderer.drawString(nameChars, context, textCenterX, this.parent.getY() + this.offset + 8, this.defaultColor.getRGB());
        this.renderHover(context, mouseX, mouseY, delta);
        this.renderSettings(context, mouseX, mouseY, delta);
        for (RenderableSetting renderableSetting : this.settings) {
            if (!this.extended) continue;
            renderableSetting.renderDescription(context, mouseX, mouseY, delta);
        }
        if (this.isHovered(mouseX, mouseY) && !this.parent.dragging) {
            CharSequence chars = this.module.getDescription();
            int tw = TextRenderer.getWidth(chars);
            int parentCenter = Argon.mc.method_22683().method_4489() / 2;
            int textCenter = parentCenter - tw / 2;
            RenderUtils.renderRoundedQuad(context.method_51448(), new Color(100, 100, 100, 100), textCenter - 5, (double)Argon.mc.method_22683().method_4506() / 2.0 + 294.0, textCenter + tw + 5, (double)Argon.mc.method_22683().method_4506() / 2.0 + 318.0, 3.0, 10.0);
            TextRenderer.drawString(chars, context, textCenter, Argon.mc.method_22683().method_4506() / 2 + 300, Color.WHITE.getRGB());
        }
    }

    private void renderHover(class_332 context, int mouseX, int mouseY, float delta) {
        if (!this.parent.dragging) {
            int toHoverAlpha = this.isHovered(mouseX, mouseY) ? 15 : 0;
            this.currentAlpha = this.currentAlpha == null ? new Color(255, 255, 255, toHoverAlpha) : new Color(255, 255, 255, this.currentAlpha.getAlpha());
            if (this.currentAlpha.getAlpha() != toHoverAlpha) {
                this.currentAlpha = ColorUtils.smoothAlphaTransition(0.05f, toHoverAlpha, this.currentAlpha);
            }
            context.method_25294(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.parent.getHeight() + this.offset, this.currentAlpha.getRGB());
        }
    }

    private void renderSettings(class_332 context, int mouseX, int mouseY, float delta) {
        int scissorX = this.parent.getX();
        int scissorY = (int)((double)Argon.mc.method_22683().method_4507() - ((double)(this.parent.getY() + this.offset) + this.animation.getValue()));
        int scissorWidth = this.parent.getWidth();
        int scissorHeight = (int)this.animation.getValue();
        RenderSystem.enableScissor((int)scissorX, (int)scissorY, (int)scissorWidth, (int)scissorHeight);
        for (RenderableSetting renderableSetting : this.settings) {
            if (!(this.animation.getValue() > (double)this.parent.getHeight())) continue;
            renderableSetting.render(context, mouseX, mouseY, delta);
        }
        for (RenderableSetting renderableSetting : this.settings) {
            if (!(this.animation.getValue() > (double)this.parent.getHeight())) continue;
            if (renderableSetting instanceof Slider) {
                Slider slider = (Slider)renderableSetting;
                RenderUtils.renderCircle(context.method_51448(), new Color(0, 0, 0, 170), (double)slider.parentX() + Math.max(slider.lerpedOffsetX, 2.5), (double)(slider.parentY() + slider.offset + slider.parentOffset()) + 27.5, 6.0, 15);
                RenderUtils.renderCircle(context.method_51448(), slider.currentColor1.brighter(), (double)slider.parentX() + Math.max(slider.lerpedOffsetX, 2.5), (double)(slider.parentY() + slider.offset + slider.parentOffset()) + 27.5, 5.0, 15);
                continue;
            }
            if (!(renderableSetting instanceof MinMaxSlider)) continue;
            MinMaxSlider slider = (MinMaxSlider)renderableSetting;
            RenderUtils.renderCircle(context.method_51448(), new Color(0, 0, 0, 170), (double)slider.parentX() + Math.max(slider.lerpedOffsetMinX, 2.5), (double)(slider.parentY() + slider.offset + slider.parentOffset()) + 27.5, 6.0, 15);
            RenderUtils.renderCircle(context.method_51448(), slider.currentColor1.brighter(), (double)slider.parentX() + Math.max(slider.lerpedOffsetMinX, 2.5), (double)(slider.parentY() + slider.offset + slider.parentOffset()) + 27.5, 5.0, 15);
            RenderUtils.renderCircle(context.method_51448(), new Color(0, 0, 0, 170), (double)slider.parentX() + Math.max(slider.lerpedOffsetMaxX, 2.5), (double)(slider.parentY() + slider.offset + slider.parentOffset()) + 27.5, 6.0, 15);
            RenderUtils.renderCircle(context.method_51448(), slider.currentColor1.brighter(), (double)slider.parentX() + Math.max(slider.lerpedOffsetMaxX, 2.5), (double)(slider.parentY() + slider.offset + slider.parentOffset()) + 27.5, 5.0, 15);
        }
        RenderSystem.disableScissor();
    }

    public void onExtend() {
        for (ModuleButton moduleButton : this.parent.moduleButtons) {
            moduleButton.extended = false;
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (RenderableSetting setting : this.settings) {
            setting.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.extended) {
            for (RenderableSetting renderableSetting : this.settings) {
                renderableSetting.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered(mouseX, mouseY)) {
            if (button == 0) {
                this.module.toggle();
            }
            if (button == 1) {
                if (this.module.getSettings().isEmpty()) {
                    return;
                }
                if (!this.extended) {
                    this.onExtend();
                }
                boolean bl = this.extended = !this.extended;
            }
        }
        if (this.extended) {
            for (RenderableSetting renderableSetting : this.settings) {
                renderableSetting.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void onGuiClose() {
        this.currentAlpha = null;
        this.currentColor = null;
        for (RenderableSetting renderableSetting : this.settings) {
            renderableSetting.onGuiClose();
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (RenderableSetting renderableSetting : this.settings) {
            renderableSetting.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > (double)this.parent.getX() && mouseX < (double)(this.parent.getX() + this.parent.getWidth()) && mouseY > (double)(this.parent.getY() + this.offset) && mouseY < (double)(this.parent.getY() + this.offset + this.parent.getHeight());
    }
}
