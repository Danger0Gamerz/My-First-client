package dev.lvstrng.argon.mixin;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.modules.render.NoBounce;
import dev.lvstrng.argon.utils.CrystalUtils;
import dev.lvstrng.argon.utils.RenderUtils;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1774;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1838;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1774.class})
public class EndCrystalItemMixin {
    @Unique
    private class_243 getPlayerLookVec(class_1657 p) {
        return RenderUtils.getPlayerLookVec(p);
    }

    @Unique
    private class_243 getClientLookVec() {
        assert (Argon.mc.field_1724 != null);
        return this.getPlayerLookVec((class_1657)Argon.mc.field_1724);
    }

    @Unique
    private boolean isBlock(class_2248 b, class_2338 p) {
        return this.getBlockState(p).method_26204() == b;
    }

    @Unique
    private class_2680 getBlockState(class_2338 p) {
        return Argon.mc.field_1687.method_8320(p);
    }

    @Unique
    private boolean canPlaceCrystalServer(class_2338 blockPos) {
        class_2680 blockState = Argon.mc.field_1687.method_8320(blockPos);
        if (!blockState.method_27852(class_2246.field_10540) && !blockState.method_27852(class_2246.field_9987)) {
            return false;
        }
        return CrystalUtils.canPlaceCrystalClientAssumeObsidian(blockPos);
    }

    @Inject(method={"useOnBlock"}, at={@At(value="HEAD")})
    private void onUse(class_1838 context, CallbackInfoReturnable<class_1269> cir) {
        class_3965 blockHit2;
        class_2338 pos;
        class_239 hitResult;
        class_243 e;
        class_3965 blockHit;
        class_1799 mainHandStack;
        NoBounce noBounce = Argon.INSTANCE.getModuleManager().getModule(NoBounce.class);
        if (noBounce.isEnabled() && Argon.INSTANCE != null && Argon.mc.field_1724 != null && (mainHandStack = Argon.mc.field_1724.method_6047()).method_31574(class_1802.field_8301) && (this.isBlock(class_2246.field_10540, (blockHit = Argon.mc.field_1687.method_17742(new class_3959(e = Argon.mc.field_1724.method_33571(), e.method_1019(this.getClientLookVec().method_1021(4.5)), class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)Argon.mc.field_1724))).method_17777()) || this.isBlock(class_2246.field_9987, blockHit.method_17777())) && (hitResult = Argon.mc.field_1765) instanceof class_3965 && this.canPlaceCrystalServer(pos = (blockHit2 = (class_3965)hitResult).method_17777())) {
            context.method_8041().method_7934(-1);
        }
    }
}
