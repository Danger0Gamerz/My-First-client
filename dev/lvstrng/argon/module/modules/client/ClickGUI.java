package dev.lvstrng.argon.module.modules.client;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.PacketReceiveListener;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import net.minecraft.class_3944;
import net.minecraft.class_437;
import net.minecraft.class_490;

public final class ClickGUI
extends Module
implements PacketReceiveListener {
    public static final NumberSetting red = new NumberSetting(EncryptedString.of("Red"), 0.0, 255.0, 255.0, 1.0);
    public static final NumberSetting green = new NumberSetting(EncryptedString.of("Green"), 0.0, 255.0, 0.0, 1.0);
    public static final NumberSetting blue = new NumberSetting(EncryptedString.of("Blue"), 0.0, 255.0, 50.0, 1.0);
    public static final NumberSetting alphaWindow = new NumberSetting(EncryptedString.of("Window Alpha"), 0.0, 255.0, 170.0, 1.0);
    public static final BooleanSetting breathing = (BooleanSetting)new BooleanSetting(EncryptedString.of("Breathing"), true).setDescription(EncryptedString.of("Color breathing effect (only with rainbow off)"));
    public static final BooleanSetting rainbow = (BooleanSetting)new BooleanSetting(EncryptedString.of("Rainbow"), true).setDescription(EncryptedString.of("Enables LGBTQ mode"));
    public static final BooleanSetting background = (BooleanSetting)new BooleanSetting(EncryptedString.of("Background"), false).setDescription(EncryptedString.of("Renders the background of the Click Gui"));
    public static final BooleanSetting customFont = new BooleanSetting(EncryptedString.of("Custom Font"), true);
    private final BooleanSetting preventClose = (BooleanSetting)new BooleanSetting(EncryptedString.of("Prevent Close"), true).setDescription(EncryptedString.of("For servers with freeze plugins that don't let you open the GUI"));
    public static final NumberSetting roundQuads = new NumberSetting(EncryptedString.of("Roundness"), 1.0, 10.0, 5.0, 1.0);
    public static final ModeSetting<AnimationMode> animationMode = new ModeSetting<AnimationMode>(EncryptedString.of("Animations"), AnimationMode.Normal, AnimationMode.class);
    public static final BooleanSetting antiAliasing = (BooleanSetting)new BooleanSetting(EncryptedString.of("MSAA"), true).setDescription(EncryptedString.of("Anti Aliasing | This can impact performance if you're using tracers but gives them a smoother look |"));

    public ClickGUI() {
        super(EncryptedString.of("Argon"), EncryptedString.of("Settings for the client"), 344, Category.CLIENT);
        this.addSettings(red, green, blue, alphaWindow, breathing, rainbow, background, this.preventClose, roundQuads, animationMode, antiAliasing);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(PacketReceiveListener.class, this);
        Argon.INSTANCE.previousScreen = this.mc.field_1755;
        if (Argon.INSTANCE.clickGui != null) {
            this.mc.method_29970((class_437)Argon.INSTANCE.clickGui);
        } else if (this.mc.field_1755 instanceof class_490) {
            Argon.INSTANCE.guiInitialized = true;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(PacketReceiveListener.class, this);
        if (this.mc.field_1755 instanceof ClickGui) {
            Argon.INSTANCE.clickGui.method_25419();
            this.mc.method_29970(Argon.INSTANCE.previousScreen);
            Argon.INSTANCE.clickGui.onGuiClose();
        } else if (this.mc.field_1755 instanceof class_490) {
            Argon.INSTANCE.guiInitialized = false;
        }
        super.onDisable();
    }

    @Override
    public void onPacketReceive(PacketReceiveListener.PacketReceiveEvent event) {
        if (Argon.INSTANCE.guiInitialized && event.packet instanceof class_3944 && this.preventClose.getValue()) {
            event.cancel();
        }
    }

    public static enum AnimationMode {
        Normal,
        Positive,
        Off;

    }
}
