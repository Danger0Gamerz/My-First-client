package dev.lvstrng.argon.mixin;

import net.minecraft.class_243;
import net.minecraft.class_745;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_745.class})
public interface OtherClientPlayerEntityAccessor {
    @Accessor(value="velocityLerpDivisor")
    public int getVelocityLerpDivisor();

    @Accessor(value="velocityLerpDivisor")
    public void setVelocityLerpDivisor(int var1);

    @Accessor(value="clientVelocity")
    public class_243 getClientVelocity();

    @Accessor(value="clientVelocity")
    public void setClientVelocity(class_243 var1);
}
