package dev.lvstrng.argon;

import dev.lvstrng.argon.event.EventManager;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.managers.FriendManager;
import dev.lvstrng.argon.managers.ProfileManager;
import dev.lvstrng.argon.module.ModuleManager;
import dev.lvstrng.argon.utils.rotation.RotatorManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import net.minecraft.class_310;
import net.minecraft.class_437;

public final class Argon {
    public RotatorManager rotatorManager;
    public ProfileManager profileManager;
    public ModuleManager moduleManager;
    public EventManager eventManager;
    public FriendManager friendManager;
    public static class_310 mc;
    public String version = " b1.3";
    public static boolean BETA;
    public static Argon INSTANCE;
    public boolean guiInitialized;
    public ClickGui clickGui;
    public class_437 previousScreen = null;
    public long lastModified;
    public File argonJar;

    public Argon() throws InterruptedException, IOException {
        INSTANCE = this;
        this.eventManager = new EventManager();
        this.moduleManager = new ModuleManager();
        this.clickGui = new ClickGui();
        this.rotatorManager = new RotatorManager();
        this.profileManager = new ProfileManager();
        this.friendManager = new FriendManager();
        this.getProfileManager().loadProfile();
        this.setLastModified();
        this.guiInitialized = false;
        mc = class_310.method_1551();
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public ClickGui getClickGui() {
        return this.clickGui;
    }

    public void resetModifiedDate() {
        this.argonJar.setLastModified(this.lastModified);
    }

    public String getVersion() {
        return this.version;
    }

    public void setLastModified() {
        try {
            this.argonJar = new File(Argon.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            this.lastModified = this.argonJar.lastModified();
        }
        catch (URISyntaxException uRISyntaxException) {
            // empty catch block
        }
    }
}
