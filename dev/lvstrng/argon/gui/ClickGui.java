package dev.lvstrng.argon.gui;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.gui.Window;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.RenderUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public final class ClickGui
extends class_437 {
    public List<Window> windows = new ArrayList<Window>();
    public Color currentColor;
    private static final StackWalker sw = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public ClickGui() {
        super((class_2561)class_2561.method_43473());
        int offsetX = 50;
        for (Category category : Category.values()) {
            this.windows.add(new Window(offsetX, 50, 230, 30, category, this));
            offsetX += 250;
        }
    }

    public boolean isDraggingAlready() {
        for (Window window : this.windows) {
            if (!window.dragging) continue;
            return true;
        }
        return false;
    }

    protected void method_56131() {
        if (this.field_22787 == null) {
            return;
        }
        super.method_56131();
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        if (Argon.mc.field_1755 == this) {
            if (Argon.INSTANCE.previousScreen != null) {
                Argon.INSTANCE.previousScreen.method_25394(context, 0, 0, delta);
            }
            this.currentColor = this.currentColor == null ? new Color(0, 0, 0, 0) : new Color(0, 0, 0, this.currentColor.getAlpha());
            if (this.currentColor.getAlpha() != (ClickGUI.background.getValue() ? 200 : 0)) {
                this.currentColor = ColorUtils.smoothAlphaTransition(0.05f, ClickGUI.background.getValue() ? 200 : 0, this.currentColor);
            }
            if (Argon.mc.field_1755 instanceof ClickGui) {
                context.method_25294(0, 0, Argon.mc.method_22683().method_4480(), Argon.mc.method_22683().method_4507(), this.currentColor.getRGB());
            }
            RenderUtils.unscaledProjection();
            super.method_25394(context, mouseX *= (int)class_310.method_1551().method_22683().method_4495(), mouseY *= (int)class_310.method_1551().method_22683().method_4495(), delta);
            for (Window window : this.windows) {
                window.render(context, mouseX, mouseY, delta);
                window.updatePosition(mouseX, mouseY, delta);
            }
            RenderUtils.scaledProjection();
        }
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        for (Window window : this.windows) {
            window.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        mouseX *= (double)((int)class_310.method_1551().method_22683().method_4495());
        mouseY *= (double)((int)class_310.method_1551().method_22683().method_4495());
        for (Window window : this.windows) {
            window.mouseClicked(mouseX, mouseY, button);
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        mouseX *= (double)((int)class_310.method_1551().method_22683().method_4495());
        mouseY *= (double)((int)class_310.method_1551().method_22683().method_4495());
        for (Window window : this.windows) {
            window.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        class_310 mc = class_310.method_1551();
        mouseY *= mc.method_22683().method_4495();
        for (Window window : this.windows) {
            window.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public boolean method_25421() {
        return false;
    }

    public void method_25419() {
        Argon.INSTANCE.getModuleManager().getModule(ClickGUI.class).setEnabledStatus(false);
        this.onGuiClose();
    }

    public void onGuiClose() {
        Argon.mc.method_29970(Argon.INSTANCE.previousScreen);
        this.currentColor = null;
        for (Window window : this.windows) {
            window.onGuiClose();
        }
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        mouseX *= (double)((int)class_310.method_1551().method_22683().method_4495());
        mouseY *= (double)((int)class_310.method_1551().method_22683().method_4495());
        for (Window window : this.windows) {
            window.mouseReleased(mouseX, mouseY, button);
        }
        return super.method_25406(mouseX, mouseY, button);
    }
}
