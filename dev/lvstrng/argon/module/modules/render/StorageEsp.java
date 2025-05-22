package dev.lvstrng.argon.module.modules.render;

import dev.lvstrng.argon.event.events.GameRenderListener;
import dev.lvstrng.argon.event.events.PacketReceiveListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.WorldUtils;
import java.awt.Color;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2605;
import net.minecraft.class_2611;
import net.minecraft.class_2627;
import net.minecraft.class_2636;
import net.minecraft.class_2637;
import net.minecraft.class_2646;
import net.minecraft.class_2818;
import net.minecraft.class_3719;
import net.minecraft.class_3866;
import net.minecraft.class_4184;
import net.minecraft.class_4587;

public final class StorageEsp
extends Module
implements GameRenderListener,
PacketReceiveListener {
    private final NumberSetting alpha = new NumberSetting(EncryptedString.of("Alpha"), 1.0, 255.0, 125.0, 1.0);
    private final BooleanSetting donutBypass = new BooleanSetting(EncryptedString.of("Donut Bypass"), false);
    private final BooleanSetting tracers = (BooleanSetting)new BooleanSetting(EncryptedString.of("Tracers"), false).setDescription(EncryptedString.of("Draws a line from your player to the storage block"));

    public StorageEsp() {
        super(EncryptedString.of("Storage ESP"), EncryptedString.of("Renders storage blocks through walls"), -1, Category.RENDER);
        this.addSettings(this.donutBypass, this.alpha, this.tracers);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(PacketReceiveListener.class, this);
        this.eventManager.add(GameRenderListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(PacketReceiveListener.class, this);
        this.eventManager.remove(GameRenderListener.class, this);
        super.onDisable();
    }

    @Override
    public void onGameRender(GameRenderListener.GameRenderEvent event) {
        this.renderStorages(event);
    }

    private Color getColor(class_2586 blockEntity, int a) {
        if (blockEntity instanceof class_2646) {
            return new Color(200, 91, 0, a);
        }
        if (blockEntity instanceof class_2595) {
            return new Color(156, 91, 0, a);
        }
        if (blockEntity instanceof class_2611) {
            return new Color(117, 0, 255, a);
        }
        if (blockEntity instanceof class_2636) {
            return new Color(138, 126, 166, a);
        }
        if (blockEntity instanceof class_2627) {
            return new Color(134, 0, 158, a);
        }
        if (blockEntity instanceof class_3866) {
            return new Color(125, 125, 125, a);
        }
        if (blockEntity instanceof class_3719) {
            return new Color(255, 140, 140, a);
        }
        if (blockEntity instanceof class_2605) {
            return new Color(80, 80, 255, a);
        }
        return new Color(255, 255, 255, 0);
    }

    private void renderStorages(GameRenderListener.GameRenderEvent event) {
        class_4184 cam = this.mc.field_1773.method_19418();
        if (cam != null) {
            class_4587 matrices = event.matrices;
            matrices.method_22903();
            class_243 vec = cam.method_19326();
            matrices.method_22904(-vec.field_1352, -vec.field_1351, -vec.field_1350);
        }
        for (class_2818 chunk : WorldUtils.getLoadedChunks().toList()) {
            for (class_2338 blockPos : chunk.method_12021()) {
                class_2586 blockEntity = this.mc.field_1687.method_8321(blockPos);
                RenderUtils.renderFilledBox(event.matrices, (float)blockPos.method_10263() + 0.1f, (float)blockPos.method_10264() + 0.05f, (float)blockPos.method_10260() + 0.1f, (float)blockPos.method_10263() + 0.9f, (float)blockPos.method_10264() + 0.85f, (float)blockPos.method_10260() + 0.9f, this.getColor(blockEntity, this.alpha.getValueInt()));
                if (!this.tracers.getValue()) continue;
                RenderUtils.renderLine(event.matrices, this.getColor(blockEntity, 255), this.mc.field_1765.method_17784(), new class_243((double)blockPos.method_10263() + 0.5, (double)blockPos.method_10264() + 0.5, (double)blockPos.method_10260() + 0.5));
            }
        }
        class_4587 matrixStack = event.matrices;
        matrixStack.method_22909();
    }

    @Override
    public void onPacketReceive(PacketReceiveListener.PacketReceiveEvent event) {
        if (this.donutBypass.getValue() && event.packet instanceof class_2637) {
            event.cancel();
        }
    }
}
