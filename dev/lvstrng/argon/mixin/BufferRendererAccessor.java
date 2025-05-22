package dev.lvstrng.argon.mixin;

import net.minecraft.class_286;
import net.minecraft.class_291;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_286.class})
public interface BufferRendererAccessor {
    @Accessor(value="currentVertexBuffer")
    public static void setCurrentVertexBuffer(class_291 vertexBuffer) {
    }
}
