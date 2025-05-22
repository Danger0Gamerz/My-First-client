package dev.lvstrng.argon.module.modules.misc;

import dev.lvstrng.argon.event.events.PacketReceiveListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import net.minecraft.class_2596;
import net.minecraft.class_2670;
import net.minecraft.class_2827;

public final class PingSpoof
extends Module
implements PacketReceiveListener {
    private final MinMaxSetting ping = (MinMaxSetting)new MinMaxSetting(EncryptedString.of("Ping"), 0.0, 1000.0, 1.0, 0.0, 600.0).setDescription(EncryptedString.of("The ping you want to achieve"));
    private int delay;

    public PingSpoof() {
        super(EncryptedString.of("Ping Spoof"), EncryptedString.of("Holds back packets making the server think your internet connection is bad."), -1, Category.MISC);
        this.addSettings(this.ping);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(PacketReceiveListener.class, this);
        this.delay = this.ping.getRandomValueInt();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(PacketReceiveListener.class, this);
        super.onDisable();
    }

    @Override
    public void onPacketReceive(PacketReceiveListener.PacketReceiveEvent event) {
        class_2596 class_25962 = event.packet;
        if (class_25962 instanceof class_2670) {
            class_2670 packet = (class_2670)class_25962;
            new Thread(() -> {
                try {
                    Thread.sleep(this.delay);
                    this.mc.method_1562().method_48296().method_10743((class_2596)new class_2827(packet.method_11517()));
                    this.delay = this.ping.getRandomValueInt();
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }).start();
            event.cancel();
        }
    }
}
