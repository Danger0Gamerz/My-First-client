package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.AttackListener;
import dev.lvstrng.argon.event.events.BlockBreakingListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import net.minecraft.class_1743;
import net.minecraft.class_1829;
import net.minecraft.class_239;

public final class NoMissDelay
extends Module
implements AttackListener,
BlockBreakingListener {
    private final BooleanSetting onlyWeapon = new BooleanSetting(EncryptedString.of("Only weapon"), true);
    private final BooleanSetting air = (BooleanSetting)new BooleanSetting(EncryptedString.of("Air"), true).setDescription(EncryptedString.of("Whether to stop hits directed to the air"));
    private final BooleanSetting blocks = (BooleanSetting)new BooleanSetting(EncryptedString.of("Blocks"), false).setDescription(EncryptedString.of("Whether to stop hits directed to blocks"));

    public NoMissDelay() {
        super(EncryptedString.of("No Miss Delay"), EncryptedString.of("Doesn't let you miss your sword/axe hits"), -1, Category.COMBAT);
        this.addSettings(this.onlyWeapon, this.air, this.blocks);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(AttackListener.class, this);
        this.eventManager.add(BlockBreakingListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(AttackListener.class, this);
        this.eventManager.remove(BlockBreakingListener.class, this);
        super.onDisable();
    }

    @Override
    public void onAttack(AttackListener.AttackEvent event) {
        if (this.onlyWeapon.getValue() && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1829) && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1743)) {
            return;
        }
        switch (this.mc.field_1765.method_17783()) {
            case field_1333: {
                if (!this.air.getValue()) break;
                event.cancel();
                break;
            }
            case field_1332: {
                if (!this.blocks.getValue()) break;
                event.cancel();
            }
        }
    }

    @Override
    public void onBlockBreaking(BlockBreakingListener.BlockBreakingEvent event) {
        if (this.onlyWeapon.getValue() && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1829) && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1743)) {
            return;
        }
        if (this.mc.field_1765.method_17783() == class_239.class_240.field_1332 && this.blocks.getValue()) {
            event.cancel();
        }
    }
}
