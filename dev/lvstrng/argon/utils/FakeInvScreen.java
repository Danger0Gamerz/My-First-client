package dev.lvstrng.argon.utils;

import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_490;

public class FakeInvScreen
extends class_490 {
    public FakeInvScreen(class_1657 player) {
        super(player);
    }

    protected void method_2383(class_1735 slot, int slotId, int button, class_1713 actionType) {
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        return false;
    }
}
