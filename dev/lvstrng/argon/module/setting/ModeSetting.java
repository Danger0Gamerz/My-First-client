package dev.lvstrng.argon.module.setting;

import dev.lvstrng.argon.module.setting.Setting;
import java.util.Arrays;
import java.util.List;

public final class ModeSetting<T extends Enum<T>>
extends Setting<ModeSetting<T>> {
    public int index;
    private final List<T> possibleValues;
    private final int originalValue;

    public ModeSetting(CharSequence name, T defaultValue, Class<T> type) {
        super(name);
        Enum[] values = (Enum[])type.getEnumConstants();
        this.possibleValues = Arrays.asList(values);
        this.originalValue = this.index = this.possibleValues.indexOf(defaultValue);
    }

    public T getMode() {
        return (T)((Enum)this.possibleValues.get(this.index));
    }

    public void setMode(T mode) {
        this.index = this.possibleValues.indexOf(mode);
    }

    public void setModeIndex(int mode) {
        this.index = mode;
    }

    public int getModeIndex() {
        return this.index;
    }

    public int getOriginalValue() {
        return this.originalValue;
    }

    public void cycle() {
        this.index = this.index < this.possibleValues.size() - 1 ? ++this.index : 0;
    }

    public boolean isMode(T mode) {
        return this.index == this.possibleValues.indexOf(mode);
    }
}
