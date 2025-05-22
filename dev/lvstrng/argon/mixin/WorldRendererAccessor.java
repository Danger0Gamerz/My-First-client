package dev.lvstrng.argon.mixin;

import net.minecraft.class_4604;
import net.minecraft.class_761;
import net.minecraft.class_769;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_761.class})
public interface WorldRendererAccessor {
    @Accessor(value="chunks")
    public class_769 getChunks();

    @Accessor
    public class_4604 getFrustum();

    @Accessor
    public void setFrustum(class_4604 var1);
}
