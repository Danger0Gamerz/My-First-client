package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.imixin.IKeyBinding;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_304.class})
public abstract class KeyBindingMixin
implements IKeyBinding {
    @Shadow
    private class_3675.class_306 field_1655;

    @Override
    public boolean isActuallyPressed() {
        long handle = Argon.mc.method_22683().method_4490();
        int code = this.field_1655.method_1444();
        return class_3675.method_15987((long)handle, (int)code);
    }

    @Override
    public void resetPressed() {
        this.method_23481(this.isActuallyPressed());
    }

    @Shadow
    public abstract void method_23481(boolean var1);
}
