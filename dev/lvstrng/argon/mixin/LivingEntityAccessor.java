package dev.lvstrng.argon.mixin;

import net.minecraft.class_1282;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1309.class})
public interface LivingEntityAccessor {
    @Accessor
    public boolean getJumping();

    @Accessor(value="lastDamageSource")
    public class_1282 getLastDamageSource();

    @Accessor(value="lastDamageSource")
    public void setLastDamageSource(class_1282 var1);

    @Accessor(value="lastDamageTime")
    public long getLastDamageTime();

    @Accessor(value="lastDamageTime")
    public void setLastDamageTime(long var1);

    @Accessor(value="lastBlockPos")
    public class_2338 getLastBlockPos();

    @Accessor(value="lastBlockPos")
    public void setLastBlockPos(class_2338 var1);

    @Accessor(value="attacking")
    public void setAttacking(class_1309 var1);
}
