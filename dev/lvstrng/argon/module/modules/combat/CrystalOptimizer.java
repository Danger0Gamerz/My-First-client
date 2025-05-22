package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.PacketSendListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.WorldUtils;
import net.minecraft.class_1268;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_3966;

public final class CrystalOptimizer
extends Module
implements PacketSendListener {
    public CrystalOptimizer() {
        super(EncryptedString.of("Crystal Optimizer"), EncryptedString.of("Makes your crystals disappear faster client-side so you can place crystals faster"), -1, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(PacketSendListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(PacketSendListener.class, this);
        super.onDisable();
    }

    @Override
    public void onPacketSend(PacketSendListener.PacketSendEvent event) {
        class_2596 class_25962 = event.packet;
        if (class_25962 instanceof class_2824) {
            class_2824 interactPacket = (class_2824)class_25962;
            interactPacket.method_34209(new class_2824.class_5908(){

                public void method_34219(class_1268 hand) {
                }

                public void method_34220(class_1268 hand, class_243 pos) {
                }

                public void method_34218() {
                    class_3966 hit;
                    class_239 class_2392;
                    if (((CrystalOptimizer)CrystalOptimizer.this).mc.field_1765 == null) {
                        return;
                    }
                    if (((CrystalOptimizer)CrystalOptimizer.this).mc.field_1765.method_17783() == class_239.class_240.field_1331 && (class_2392 = ((CrystalOptimizer)CrystalOptimizer.this).mc.field_1765) instanceof class_3966 && (hit = (class_3966)class_2392).method_17782() instanceof class_1511) {
                        class_1293 weakness = ((CrystalOptimizer)CrystalOptimizer.this).mc.field_1724.method_6112(class_1294.field_5911);
                        class_1293 strength = ((CrystalOptimizer)CrystalOptimizer.this).mc.field_1724.method_6112(class_1294.field_5910);
                        if (!(weakness == null || strength != null && strength.method_5578() > weakness.method_5578() || WorldUtils.isTool(((CrystalOptimizer)CrystalOptimizer.this).mc.field_1724.method_6047()))) {
                            return;
                        }
                        hit.method_17782().method_5768();
                        hit.method_17782().method_31745(class_1297.class_5529.field_26998);
                        hit.method_17782().method_36209();
                    }
                }
            });
        }
    }
}
