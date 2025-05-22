package dev.lvstrng.argon.module.modules.client;

import com.sun.jna.Memory;
import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.gui.ClickGui;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.module.setting.StringSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.Utils;
import java.io.File;

public final class SelfDestruct
extends Module {
    public static boolean destruct = false;
    private final BooleanSetting replaceMod = (BooleanSetting)new BooleanSetting(EncryptedString.of("Replace Mod"), true).setDescription(EncryptedString.of("Repalces the mod with the original JAR file of the ImmediatelyFast mod"));
    private final BooleanSetting saveLastModified = (BooleanSetting)new BooleanSetting(EncryptedString.of("Save Last Modified"), true).setDescription(EncryptedString.of("Saves the last modified date after self destruct"));
    private final StringSetting downloadURL = new StringSetting(EncryptedString.of("Replace URL"), "https://cdn.modrinth.com/data/5ZwdcRci/versions/FEOsWs1E/ImmediatelyFast-Fabric-1.2.11%2B1.20.4.jar");

    public SelfDestruct() {
        super(EncryptedString.of("Self Destruct"), EncryptedString.of("Removes the client from your game |Credits to lwes for deletion|"), -1, Category.CLIENT);
        this.addSettings(this.replaceMod, this.saveLastModified, this.downloadURL);
    }

    @Override
    public void onEnable() {
        destruct = true;
        Argon.INSTANCE.getModuleManager().getModule(ClickGUI.class).setEnabled(false);
        this.setEnabled(false);
        Argon.INSTANCE.getProfileManager().saveProfile();
        if (this.mc.field_1755 instanceof ClickGui) {
            Argon.INSTANCE.guiInitialized = false;
            this.mc.field_1755.method_25419();
        }
        if (this.replaceMod.getValue()) {
            try {
                String modUrl = this.downloadURL.getValue();
                File currentJar = Utils.getCurrentJarPath();
                if (currentJar.exists()) {
                    Utils.replaceModFile(modUrl, Utils.getCurrentJarPath());
                }
            }
            catch (Exception modUrl) {
                // empty catch block
            }
        }
        for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
            module.setEnabled(false);
            module.setName(null);
            module.setDescription(null);
            for (Setting<?> setting : module.getSettings()) {
                setting.setName(null);
                setting.setDescription(null);
                if (!(setting instanceof StringSetting)) continue;
                StringSetting set = (StringSetting)setting;
                set.setValue(null);
            }
            module.getSettings().clear();
        }
        Runtime runtime = Runtime.getRuntime();
        if (this.saveLastModified.getValue()) {
            Argon.INSTANCE.resetModifiedDate();
        }
        for (int i = 0; i <= 10; ++i) {
            runtime.gc();
            runtime.runFinalization();
            try {
                Thread.sleep(100 * i);
                Memory.purge();
                Memory.disposeAll();
                continue;
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }
}
