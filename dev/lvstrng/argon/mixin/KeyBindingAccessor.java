package dev.lvstrng.argon.mixin;

import net.minecraft.class_304;
import net.minecraft.class_3675;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_304.class})
public interface KeyBindingAccessor {
    @Accessor(value="boundKey")
    public class_3675.class_306 getBoundKey();
}
