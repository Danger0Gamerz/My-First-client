package dev.lvstrng.argon.gui;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.gui.components.ModuleButton;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.utils.ColorUtils;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;

public final class Window {
    public List<ModuleButton> moduleButtons = new ArrayList<ModuleButton>();
    public int x;
    public int y;
    private final int width;
    private final int height;
    public Color currentColor;
    private final Category category;
    public boolean dragging;
    public boolean extended;
    private int dragX;
    private int dragY;
    private int prevX;
    private int prevY;
    public ClickGui parent;

    public Window(int x, int y, int width, int height, Category category, ClickGui parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.dragging = false;
        this.extended = true;
        this.height = height;
        this.category = category;
        this.parent = parent;
        this.prevX = x;
        this.prevY = y;
        int offset = height;
        ArrayList<Module> sortedModules = new ArrayList<Module>(Argon.INSTANCE.getModuleManager().getModulesInCategory(category));
        for (Module module : sortedModules) {
            this.moduleButtons.add(new ModuleButton(this, module, offset));
            offset += height;
        }
    }

    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        int toAlpha = ClickGUI.alphaWindow.getValueInt();
        this.currentColor = this.currentColor == null ? new Color(0, 0, 0, 0) : new Color(0, 0, 0, this.currentColor.getAlpha());
        if (this.currentColor.getAlpha() != toAlpha) {
            this.currentColor = ColorUtils.smoothAlphaTransition(0.05f, toAlpha, this.currentColor);
        }
        RenderUtils.renderRoundedQuad(context.method_51448(), this.currentColor, this.prevX, this.prevY, this.prevX + this.width, this.prevY + this.height, ClickGUI.roundQuads.getValueInt(), ClickGUI.roundQuads.getValueInt(), 0.0, 0.0, 50.0);
        context.method_25294(this.prevX, this.prevY + (this.height - 2), this.prevX + this.width, this.prevY + this.height, Utils.getMainColor(255, this.moduleButtons.indexOf(this.moduleButtons.get(0))).getRGB());
        int charOffset = this.prevX + this.width / 2;
        int totalWidth = TextRenderer.getWidth(this.category.name);
        int startX = charOffset - totalWidth / 2;
        TextRenderer.drawString(this.category.name, context, startX, this.prevY + 6, Color.WHITE.getRGB());
        this.updateButtons(delta);
        for (ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.render(context, mouseX, mouseY, delta);
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void onGuiClose() {
        this.currentColor = null;
        for (ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.onGuiClose();
        }
        this.dragging = false;
    }

    public boolean isDraggingAlready() {
        for (Window window : this.parent.windows) {
            if (!window.dragging) continue;
            return true;
        }
        return false;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered(mouseX, mouseY)) {
            switch (button) {
                case 0: {
                    if (this.parent.isDraggingAlready()) break;
                    this.dragging = true;
                    this.dragX = (int)(mouseX - (double)this.x);
                    this.dragY = (int)(mouseY - (double)this.y);
                    break;
                }
                case 1: {
                    if (this.dragging) break;
                }
            }
        }
        if (this.extended) {
            for (ModuleButton moduleButton : this.moduleButtons) {
                moduleButton.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.extended) {
            for (ModuleButton moduleButton : this.moduleButtons) {
                moduleButton.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
    }

    public void updateButtons(float delta) {
        int offset = this.height;
        for (ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.animation.animate(0.5 * (double)delta, moduleButton.extended ? (double)(this.height * (moduleButton.settings.size() + 1)) : (double)this.height);
            double supHeight = moduleButton.animation.getValue();
            moduleButton.offset = offset;
            offset += (int)supHeight;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && this.dragging) {
            this.dragging = false;
        }
        for (ModuleButton moduleButton : this.moduleButtons) {
            moduleButton.mouseReleased(mouseX, mouseY, button);
        }
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.prevX = this.x;
        this.prevY = this.y;
        this.prevY = (int)((double)this.prevY + verticalAmount * 20.0);
        this.setY((int)((double)this.y + verticalAmount * 20.0));
    }

    public int getX() {
        return this.prevX;
    }

    public int getY() {
        return this.prevY;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > (double)this.x && mouseX < (double)(this.x + this.width) && mouseY > (double)this.y && mouseY < (double)(this.y + this.height);
    }

    public boolean isPrevHovered(double mouseX, double mouseY) {
        return mouseX > (double)this.prevX && mouseX < (double)(this.prevX + this.width) && mouseY > (double)this.prevY && mouseY < (double)(this.prevY + this.height);
    }

    public void updatePosition(double mouseX, double mouseY, float delta) {
        this.prevX = this.x;
        this.prevY = this.y;
        if (this.dragging) {
            this.x = (int)MathUtils.goodLerp(0.3f * delta, this.isHovered(mouseX, mouseY) ? (double)this.x : (double)this.prevX, mouseX - (double)this.dragX);
            this.y = (int)MathUtils.goodLerp(0.3f * delta, this.isHovered(mouseX, mouseY) ? (double)this.y : (double)this.prevY, mouseY - (double)this.dragY);
        }
    }
}
