package dev.lvstrng.argon.mixin;

import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_310.class})
public interface MinecraftClientAccessor {
    @Accessor
    public class_312 getMouse();

    @Invoker
    public void invokeDoItemUse();

    @Invoker
    public boolean invokeDoAttack();

    @Accessor(value="itemUseCooldown")
    public void setItemUseCooldown(int var1);
}
