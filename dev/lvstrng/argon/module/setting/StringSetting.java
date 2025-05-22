package dev.lvstrng.argon.module.setting;

import dev.lvstrng.argon.module.setting.Setting;

public class StringSetting
extends Setting<StringSetting> {
    public String value;

    public StringSetting(CharSequence name, String defaultValue) {
        super(name);
        this.value = defaultValue;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
