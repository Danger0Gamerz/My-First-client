package dev.lvstrng.argon.imixin;

import net.minecraft.class_2382;
import org.joml.Vector3d;

public interface IVec3d {
    public void set(double var1, double var3, double var5);

    default public void set(class_2382 vec) {
        this.set(vec.method_10263(), vec.method_10264(), vec.method_10260());
    }

    default public void set(Vector3d vec) {
        this.set(vec.x, vec.y, vec.z);
    }

    public void setXZ(double var1, double var3);

    public void setY(double var1);
}
