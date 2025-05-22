package dev.lvstrng.argon.mixin;

import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_312.class})
public interface MouseHandlerAccessor {
    @Invoker(value="onMouseButton")
    public void press(long var1, int var3, int var4, int var5);
}
