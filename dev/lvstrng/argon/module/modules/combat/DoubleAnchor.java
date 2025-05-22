package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.TickListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.utils.BlockUtils;
import dev.lvstrng.argon.utils.EncryptedString;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_2596;
import net.minecraft.class_2885;
import net.minecraft.class_3965;
import org.lwjgl.glfw.GLFW;

public final class DoubleAnchor
extends Module
implements TickListener {
    private class_2338 pos;
    private int count;

    public DoubleAnchor() {
        super(EncryptedString.of("Double Anchor"), EncryptedString.of("Helps you do the air place/double anchor"), -1, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(TickListener.class, this);
        this.pos = null;
        this.count = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.mc.field_1755 == null) {
            assert (this.mc.field_1724 != null);
            if (this.mc.field_1724.method_6047().method_31574(class_1802.field_23141)) {
                class_3965 h;
                assert (this.mc.field_1687 != null);
                class_239 class_2392 = this.mc.field_1765;
                if (class_2392 instanceof class_3965 && BlockUtils.isAnchorCharged((h = (class_3965)class_2392).method_17777()) && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)1) == 1) {
                    if (h.method_17777().equals((Object)this.pos)) {
                        if (this.count >= 1) {
                            return;
                        }
                    } else {
                        this.pos = h.method_17777();
                        this.count = 0;
                    }
                    this.mc.method_1562().method_52787((class_2596)new class_2885(class_1268.field_5808, h, 0));
                    ++this.count;
                }
            }
        }
    }
}
