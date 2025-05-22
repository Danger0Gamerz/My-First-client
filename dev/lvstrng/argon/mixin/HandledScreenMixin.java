package dev.lvstrng.argon.mixin;

import net.minecraft.class_1735;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_465.class})
public interface HandledScreenMixin {
    @Accessor
    public class_1735 getFocusedSlot();
}
