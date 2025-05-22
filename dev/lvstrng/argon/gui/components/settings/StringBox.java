package dev.lvstrng.argon.gui.components.settings;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.gui.components.ModuleButton;
import dev.lvstrng.argon.gui.components.settings.RenderableSetting;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.module.setting.StringSetting;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;

public final class StringBox
extends RenderableSetting {
    private final StringSetting setting;
    private Color currentAlpha;

    public StringBox(ModuleButton parent, Setting<?> setting, int offset) {
        super(parent, setting, offset);
        this.setting = (StringSetting)setting;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        TextRenderer.drawString(String.valueOf(this.setting.getName()) + ": " + (String)(this.setting.getValue().length() <= 9 ? this.setting.getValue() : this.setting.getValue().substring(0, 9) + "..."), context, this.parentX() + 9, this.parentY() + this.parentOffset() + this.offset + 9, new Color(245, 245, 245, 255).getRGB());
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
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.mc.method_1507(new class_437((class_2561)class_2561.method_43473()){
                private String content;
                {
                    this.content = StringBox.this.setting.getValue();
                }

                public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
                    RenderUtils.unscaledProjection();
                    super.method_25394(context, mouseX *= (int)class_310.method_1551().method_22683().method_4495(), mouseY *= (int)class_310.method_1551().method_22683().method_4495(), delta);
                    context.method_25294(0, 0, StringBox.this.mc.method_22683().method_4480(), StringBox.this.mc.method_22683().method_4507(), new Color(0, 0, 0, ClickGUI.background.getValue() ? 200 : 0).getRGB());
                    int screenMidX = StringBox.this.mc.method_22683().method_4480() / 2;
                    int screenMidY = StringBox.this.mc.method_22683().method_4507() / 2;
                    int contentWidth = Math.max(TextRenderer.getWidth(this.content), 600);
                    int width = contentWidth + 30;
                    int startX = screenMidX - width / 2;
                    int startY = screenMidY - 30;
                    RenderUtils.renderRoundedQuad(context.method_51448(), new Color(0, 0, 0, ClickGUI.alphaWindow.getValueInt()), startX, startY, startX + width, screenMidY + 30, 5.0, 5.0, 0.0, 0.0, 20.0);
                    TextRenderer.drawCenteredString(StringBox.this.setting.getName(), context, screenMidX, startY + 10, new Color(245, 245, 245, 255).getRGB());
                    context.method_25294(startX, screenMidY, startX + width, screenMidY + 30, new Color(0, 0, 0, 120).getRGB());
                    RenderUtils.renderRoundedOutline(context, new Color(50, 50, 50, 255), startX + 10, screenMidY + 5, startX + (width - 10), screenMidY + 25, 5.0, 5.0, 5.0, 5.0, 2.0, 20.0);
                    TextRenderer.drawString(this.content, context, startX + 15, screenMidY + 8, new Color(245, 245, 245, 255).getRGB());
                    context.method_25294(startX, screenMidY, startX + width, screenMidY + 1, Utils.getMainColor(255, 1).getRGB());
                    RenderUtils.scaledProjection();
                }

                public boolean method_25404(int keyCode, int scanCode, int modifiers) {
                    if (keyCode == 256) {
                        StringBox.this.setting.setValue(this.content.strip());
                        StringBox.this.mc.method_1507((class_437)Argon.INSTANCE.clickGui);
                    }
                    if (1.method_25437((int)keyCode)) {
                        this.content = this.content + StringBox.this.mc.field_1774.method_1460();
                    }
                    if (1.method_25438((int)keyCode)) {
                        GLFW.glfwSetClipboardString((long)StringBox.this.mc.method_22683().method_4490(), (CharSequence)this.content);
                    }
                    if (keyCode == 259 && !this.content.isEmpty()) {
                        this.content = this.content.substring(0, this.content.length() - 1);
                    }
                    return super.method_25404(keyCode, scanCode, modifiers);
                }

                public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
                }

                public boolean method_25400(char chr, int modifiers) {
                    this.content = this.content + chr;
                    return super.method_25400(chr, modifiers);
                }

                public boolean method_25422() {
                    return false;
                }
            });
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}
