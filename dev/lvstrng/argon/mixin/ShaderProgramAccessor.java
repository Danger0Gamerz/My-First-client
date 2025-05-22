package dev.lvstrng.argon.mixin;

import java.util.Map;
import net.minecraft.class_284;
import net.minecraft.class_5944;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_5944.class})
public interface ShaderProgramAccessor {
    @Accessor(value="loadedUniforms")
    public Map<String, class_284> getLoadedUniforms();
}
