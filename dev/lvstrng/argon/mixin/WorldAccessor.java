package dev.lvstrng.argon.mixin;

import java.util.List;
import net.minecraft.class_1937;
import net.minecraft.class_5562;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1937.class})
public interface WorldAccessor {
    @Accessor(value="blockEntityTickers")
    public List<class_5562> getBlockEntityTickers();
}
