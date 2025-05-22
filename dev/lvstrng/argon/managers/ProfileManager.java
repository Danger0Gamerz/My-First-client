package dev.lvstrng.argon.managers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.KeybindSetting;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.module.setting.StringSetting;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

public final class ProfileManager {
    private final Gson g = new Gson();
    private Path profileFolderPath;
    private Path profilePath;
    private String temp = System.getProperty("java.io.tmpdir");
    private String folderName = "UJHfsGGjbPfVZ";
    Path folder;
    private JsonObject profile;

    public ProfileManager() {
        this.profileFolderPath = this.folder = Paths.get(this.temp, this.folderName);
        this.profilePath = this.profileFolderPath.resolve("a.json");
    }

    public void loadProfile() {
        try {
            if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                this.temp = System.getProperty("user.home");
                this.folderName = "UJHfsGGjbPfVZ";
                this.profileFolderPath = this.folder;
                this.profilePath = this.profileFolderPath.resolve("a.json");
                if (!Files.isRegularFile(this.profilePath, new LinkOption[0])) {
                    return;
                }
                this.profile = (JsonObject)this.g.fromJson(Files.readString(this.profilePath), JsonObject.class);
                for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
                    JsonObject moduleConfig;
                    JsonElement enabledJson;
                    JsonElement moduleJson = this.profile.get(String.valueOf(Argon.INSTANCE.getModuleManager().getModules().indexOf(module)));
                    if (moduleJson == null || !moduleJson.isJsonObject() || (enabledJson = (moduleConfig = moduleJson.getAsJsonObject()).get("enabled")) == null || !enabledJson.isJsonPrimitive()) continue;
                    if (enabledJson.getAsBoolean()) {
                        module.setEnabled(true);
                    }
                    for (Setting<?> setting : module.getSettings()) {
                        JsonElement settingJson = moduleConfig.get(String.valueOf(module.getSettings().indexOf(setting)));
                        if (settingJson == null) continue;
                        if (setting instanceof BooleanSetting) {
                            BooleanSetting booleanSetting = (BooleanSetting)setting;
                            booleanSetting.setValue(settingJson.getAsBoolean());
                            continue;
                        }
                        if (setting instanceof ModeSetting) {
                            ModeSetting modeSetting = (ModeSetting)setting;
                            modeSetting.setModeIndex(settingJson.getAsInt());
                            continue;
                        }
                        if (setting instanceof NumberSetting) {
                            NumberSetting numberSetting = (NumberSetting)setting;
                            numberSetting.setValue(settingJson.getAsDouble());
                            continue;
                        }
                        if (setting instanceof KeybindSetting) {
                            KeybindSetting keybindSetting = (KeybindSetting)setting;
                            keybindSetting.setKey(settingJson.getAsInt());
                            if (!keybindSetting.isModuleKey()) continue;
                            module.setKey(settingJson.getAsInt());
                            continue;
                        }
                        if (setting instanceof StringSetting) {
                            StringSetting stringSetting = (StringSetting)setting;
                            stringSetting.setValue(settingJson.getAsString());
                            continue;
                        }
                        if (!(setting instanceof MinMaxSetting)) continue;
                        MinMaxSetting minMaxSetting = (MinMaxSetting)setting;
                        if (!settingJson.isJsonObject()) continue;
                        JsonObject minMaxObject = settingJson.getAsJsonObject();
                        double minValue = minMaxObject.get("1").getAsDouble();
                        double maxValue = minMaxObject.get("2").getAsDouble();
                        minMaxSetting.setMinValue(minValue);
                        minMaxSetting.setMaxValue(maxValue);
                    }
                }
            } else {
                if (!Files.isRegularFile(this.profilePath, new LinkOption[0])) {
                    return;
                }
                this.profile = (JsonObject)this.g.fromJson(Files.readString(this.profilePath), JsonObject.class);
                for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
                    JsonObject moduleConfig;
                    JsonElement enabledJson;
                    JsonElement moduleJson = this.profile.get(String.valueOf(Argon.INSTANCE.getModuleManager().getModules().indexOf(module)));
                    if (moduleJson == null || !moduleJson.isJsonObject() || (enabledJson = (moduleConfig = moduleJson.getAsJsonObject()).get("enabled")) == null || !enabledJson.isJsonPrimitive()) continue;
                    if (enabledJson.getAsBoolean()) {
                        module.setEnabled(true);
                    }
                    for (Setting<?> setting : module.getSettings()) {
                        JsonElement settingJson = moduleConfig.get(String.valueOf(module.getSettings().indexOf(setting)));
                        if (settingJson == null) continue;
                        if (setting instanceof BooleanSetting) {
                            BooleanSetting booleanSetting = (BooleanSetting)setting;
                            booleanSetting.setValue(settingJson.getAsBoolean());
                            continue;
                        }
                        if (setting instanceof ModeSetting) {
                            ModeSetting modeSetting = (ModeSetting)setting;
                            modeSetting.setModeIndex(settingJson.getAsInt());
                            continue;
                        }
                        if (setting instanceof NumberSetting) {
                            NumberSetting numberSetting = (NumberSetting)setting;
                            numberSetting.setValue(settingJson.getAsDouble());
                            continue;
                        }
                        if (setting instanceof KeybindSetting) {
                            KeybindSetting keybindSetting = (KeybindSetting)setting;
                            keybindSetting.setKey(settingJson.getAsInt());
                            if (!keybindSetting.isModuleKey()) continue;
                            module.setKey(settingJson.getAsInt());
                            continue;
                        }
                        if (setting instanceof StringSetting) {
                            StringSetting stringSetting = (StringSetting)setting;
                            stringSetting.setValue(settingJson.getAsString());
                            continue;
                        }
                        if (!(setting instanceof MinMaxSetting)) continue;
                        MinMaxSetting minMaxSetting = (MinMaxSetting)setting;
                        if (!settingJson.isJsonObject()) continue;
                        JsonObject minMaxObject = settingJson.getAsJsonObject();
                        double minValue = minMaxObject.get("1").getAsDouble();
                        double maxValue = minMaxObject.get("2").getAsDouble();
                        minMaxSetting.setMinValue(minValue);
                        minMaxSetting.setMaxValue(maxValue);
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void saveProfile() {
        try {
            if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                this.temp = System.getProperty("user.home");
                this.folderName = "UJHfsGGjbPfVZ";
                this.profileFolderPath = this.folder;
                this.profilePath = this.profileFolderPath.resolve("a.json");
                Files.createDirectories(this.profileFolderPath, new FileAttribute[0]);
                this.profile = new JsonObject();
                for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
                    JsonObject moduleConfig = new JsonObject();
                    moduleConfig.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
                    for (Setting<?> setting : module.getSettings()) {
                        if (setting instanceof BooleanSetting) {
                            BooleanSetting booleanSetting = (BooleanSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), Boolean.valueOf(booleanSetting.getValue()));
                            continue;
                        }
                        if (setting instanceof ModeSetting) {
                            ModeSetting modeSetting = (ModeSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), (Number)modeSetting.getModeIndex());
                            continue;
                        }
                        if (setting instanceof NumberSetting) {
                            NumberSetting numberSetting = (NumberSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), (Number)numberSetting.getValue());
                            continue;
                        }
                        if (setting instanceof KeybindSetting) {
                            KeybindSetting keybindSetting = (KeybindSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), (Number)keybindSetting.getKey());
                            continue;
                        }
                        if (setting instanceof StringSetting) {
                            StringSetting stringSetting = (StringSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), stringSetting.getValue());
                            continue;
                        }
                        if (!(setting instanceof MinMaxSetting)) continue;
                        MinMaxSetting minMaxSetting = (MinMaxSetting)setting;
                        JsonObject minMaxObject = new JsonObject();
                        minMaxObject.addProperty("1", (Number)minMaxSetting.getMinValue());
                        minMaxObject.addProperty("2", (Number)minMaxSetting.getMaxValue());
                        moduleConfig.add(String.valueOf(module.getSettings().indexOf(setting)), (JsonElement)minMaxObject);
                    }
                    this.profile.add(String.valueOf(Argon.INSTANCE.getModuleManager().getModules().indexOf(module)), (JsonElement)moduleConfig);
                }
                Files.writeString(this.profilePath, (CharSequence)this.g.toJson((JsonElement)this.profile), new OpenOption[0]);
            } else {
                Files.createDirectories(this.profileFolderPath, new FileAttribute[0]);
                this.profile = new JsonObject();
                for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
                    JsonObject moduleConfig = new JsonObject();
                    moduleConfig.addProperty("enabled", Boolean.valueOf(module.isEnabled()));
                    for (Setting<?> setting : module.getSettings()) {
                        if (setting instanceof BooleanSetting) {
                            BooleanSetting booleanSetting = (BooleanSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), Boolean.valueOf(booleanSetting.getValue()));
                            continue;
                        }
                        if (setting instanceof ModeSetting) {
                            ModeSetting modeSetting = (ModeSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), (Number)modeSetting.getModeIndex());
                            continue;
                        }
                        if (setting instanceof NumberSetting) {
                            NumberSetting numberSetting = (NumberSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), (Number)numberSetting.getValue());
                            continue;
                        }
                        if (setting instanceof KeybindSetting) {
                            KeybindSetting keybindSetting = (KeybindSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), (Number)keybindSetting.getKey());
                            continue;
                        }
                        if (setting instanceof StringSetting) {
                            StringSetting stringSetting = (StringSetting)setting;
                            moduleConfig.addProperty(String.valueOf(module.getSettings().indexOf(setting)), stringSetting.getValue());
                            continue;
                        }
                        if (!(setting instanceof MinMaxSetting)) continue;
                        MinMaxSetting minMaxSetting = (MinMaxSetting)setting;
                        JsonObject minMaxObject = new JsonObject();
                        minMaxObject.addProperty("1", (Number)minMaxSetting.getMinValue());
                        minMaxObject.addProperty("2", (Number)minMaxSetting.getMaxValue());
                        moduleConfig.add(String.valueOf(module.getSettings().indexOf(setting)), (JsonElement)minMaxObject);
                    }
                    this.profile.add(String.valueOf(Argon.INSTANCE.getModuleManager().getModules().indexOf(module)), (JsonElement)moduleConfig);
                }
                Files.writeString(this.profilePath, (CharSequence)this.g.toJson((JsonElement)this.profile), new OpenOption[0]);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
