package dev.lvstrng.argon.module.modules.render;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.HudListener;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.TextRenderer;
import dev.lvstrng.argon.utils.Utils;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_640;

public final class HUD
extends Module
implements HudListener {
    private static final CharSequence argon = EncryptedString.of("xeda-e |");
    private final BooleanSetting info = new BooleanSetting(EncryptedString.of("Info"), true);
    private final BooleanSetting modules = (BooleanSetting)new BooleanSetting("Modules", true).setDescription(EncryptedString.of("Renders module array list"));

    public HUD() {
        super(EncryptedString.of("HUD"), EncryptedString.of("Renders the client version and enabled modules on the HUD"), -1, Category.RENDER);
        this.addSettings(this.info, this.modules);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(HudListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(HudListener.class, this);
        super.onDisable();
    }

    @Override
    public void onRenderHud(HudListener.HudEvent event) {
        if (this.mc.field_1755 != Argon.INSTANCE.clickGui) {
            List enabledModules = Argon.INSTANCE.getModuleManager().getEnabledModules().stream().sorted((module1, module2) -> {
                CharSequence name1 = module1.getName();
                CharSequence name2 = module2.getName();
                int filteredLength1 = TextRenderer.getWidth(name1);
                int filteredLength2 = TextRenderer.getWidth(name2);
                return Integer.compare(filteredLength2, filteredLength1);
            }).toList();
            class_332 context = event.context;
            boolean customFont = ClickGUI.customFont.getValue();
            if (!(this.mc.field_1755 instanceof ClickGui)) {
                if (this.info.getValue() && this.mc.field_1724 != null) {
                    class_640 entry;
                    String server;
                    RenderUtils.unscaledProjection();
                    int argonOffset = 10;
                    int argonOffset2 = 10 + TextRenderer.getWidth(argon);
                    Object ping = "Ping: ";
                    String fps = "FPS: " + this.mc.method_47599() + " |";
                    String string = server = this.mc.method_1558() == null ? "None" : this.mc.method_1558().field_3761;
                    ping = this.mc != null && this.mc.field_1724 != null && this.mc.method_1562() != null ? ((entry = this.mc.method_1562().method_2871(this.mc.field_1724.method_5667())) != null ? (String)ping + entry.method_2959() + " |" : (String)ping + "N/A |") : (String)ping + "N/A |";
                    RenderUtils.renderRoundedQuad(context.method_51448(), new Color(35, 35, 35, 255), 5.0, 6.0, argonOffset2 + TextRenderer.getWidth(fps) + TextRenderer.getWidth((CharSequence)ping) + TextRenderer.getWidth(server) + 35, 30.0, 5.0, 15.0);
                    TextRenderer.drawString(argon, context, argonOffset, 12, Utils.getMainColor(255, 4).getRGB());
                    TextRenderer.drawString(fps, context, (argonOffset += TextRenderer.getWidth(argon)) + 10, 12, Utils.getMainColor(255, 3).getRGB());
                    TextRenderer.drawString((CharSequence)ping, context, argonOffset + 10 + TextRenderer.getWidth(fps) + 10, 12, Utils.getMainColor(255, 2).getRGB());
                    TextRenderer.drawString(server, context, argonOffset + 10 + TextRenderer.getWidth(fps) + TextRenderer.getWidth((CharSequence)ping) + 20, 12, Utils.getMainColor(255, 1).getRGB());
                    RenderUtils.scaledProjection();
                }
                if (this.modules.getValue()) {
                    int offset = 55;
                    for (Module module : enabledModules) {
                        RenderUtils.unscaledProjection();
                        int charOffset = 6 + TextRenderer.getWidth(module.getName());
                        class_4587 class_45872 = context.method_51448();
                        Color color = new Color(0, 0, 0, 175);
                        double d = offset - 4;
                        double d2 = charOffset + 5;
                        Objects.requireNonNull(this.mc.field_1772);
                        RenderUtils.renderRoundedQuad(class_45872, color, 0.0, d, d2, offset + 9 * 2 - 1, 0.0, 0.0, 0.0, 5.0, 10.0);
                        Objects.requireNonNull(this.mc.field_1772);
                        context.method_25296(0, offset - 4, 2, offset + 9 * 2, Utils.getMainColor(255, enabledModules.indexOf(module)).getRGB(), Utils.getMainColor(255, enabledModules.indexOf(module) + 1).getRGB());
                        int charOffset2 = customFont ? 5 : 8;
                        TextRenderer.drawString(module.getName(), context, charOffset2, offset + (customFont ? 1 : 0), Utils.getMainColor(255, enabledModules.indexOf(module)).getRGB());
                        Objects.requireNonNull(this.mc.field_1772);
                        offset += 9 * 2 + 3;
                        RenderUtils.scaledProjection();
                    }
                }
            }
        }
    }
}
