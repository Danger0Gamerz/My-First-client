package dev.lvstrng.argon.mixin;

import net.minecraft.class_4604;
import org.joml.FrustumIntersection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_4604.class})
public interface FrustumAccessor {
    @Accessor
    public FrustumIntersection getFrustumIntersection();

    @Accessor
    public void setFrustumIntersection(FrustumIntersection var1);

    @Accessor(value="x")
    public double getX();

    @Accessor(value="x")
    public void setX(double var1);

    @Accessor(value="y")
    public double getY();

    @Accessor(value="y")
    public void setY(double var1);

    @Accessor(value="z")
    public double getZ();

    @Accessor(value="z")
    public void setZ(double var1);
}
