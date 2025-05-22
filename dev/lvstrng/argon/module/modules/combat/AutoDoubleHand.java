package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.HudListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.combat.AutoCrystal;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.BlockUtils;
import dev.lvstrng.argon.utils.CrystalUtils;
import dev.lvstrng.argon.utils.DamageUtils;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.InventoryUtils;
import dev.lvstrng.argon.utils.RotationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_3965;

public final class AutoDoubleHand
extends Module
implements HudListener {
    private final BooleanSetting stopOnCrystal = (BooleanSetting)new BooleanSetting(EncryptedString.of("Stop On Crystal"), false).setDescription(EncryptedString.of("Stops while Auto Crystal is running"));
    private final BooleanSetting checkShield = (BooleanSetting)new BooleanSetting(EncryptedString.of("Check Shield"), false).setDescription(EncryptedString.of("Checks if you're blocking with a shield"));
    private final BooleanSetting onPop = (BooleanSetting)new BooleanSetting(EncryptedString.of("On Pop"), false).setDescription(EncryptedString.of("Switches to a totem if you pop"));
    private final BooleanSetting onHealth = (BooleanSetting)new BooleanSetting(EncryptedString.of("On Health"), false).setDescription(EncryptedString.of("Switches to totem if low on health"));
    private final BooleanSetting predict = new BooleanSetting(EncryptedString.of("Predict Damage"), true);
    private final NumberSetting health = (NumberSetting)new NumberSetting(EncryptedString.of("Health"), 1.0, 20.0, 2.0, 1.0).setDescription(EncryptedString.of("Health to trigger at"));
    private final BooleanSetting onGround = (BooleanSetting)new BooleanSetting(EncryptedString.of("On Ground"), true).setDescription(EncryptedString.of("Whether crystal damage is checked on ground or not"));
    private final BooleanSetting checkPlayers = (BooleanSetting)new BooleanSetting(EncryptedString.of("Check Players"), true).setDescription(EncryptedString.of("Checks for nearby players"));
    private final NumberSetting distance = (NumberSetting)new NumberSetting(EncryptedString.of("Distance"), 1.0, 10.0, 5.0, 0.1).setDescription(EncryptedString.of("Player distance"));
    private final BooleanSetting predictCrystals = new BooleanSetting(EncryptedString.of("Predict Crystals"), false);
    private final BooleanSetting checkAim = (BooleanSetting)new BooleanSetting(EncryptedString.of("Check Aim"), false).setDescription(EncryptedString.of("Checks if the opponent is aiming at obsidian"));
    private final BooleanSetting checkItems = (BooleanSetting)new BooleanSetting(EncryptedString.of("Check Items"), false).setDescription(EncryptedString.of("Checks if the opponent is holding crystals"));
    private final NumberSetting activatesAbove = (NumberSetting)new NumberSetting(EncryptedString.of("Activates Above"), 0.0, 4.0, 0.2, 0.1).setDescription(EncryptedString.of("Height to trigger at"));
    private boolean belowHealth;
    private boolean offhandHasNoTotem;

    public AutoDoubleHand() {
        super(EncryptedString.of("Auto Double Hand"), EncryptedString.of("Automatically switches to your totem when you're about to pop"), -1, Category.COMBAT);
        this.addSettings(this.stopOnCrystal, this.checkShield, this.onPop, this.onHealth, this.predict, this.health, this.onGround, this.checkPlayers, this.distance, this.predictCrystals, this.checkAim, this.checkItems, this.activatesAbove);
        this.belowHealth = false;
        this.offhandHasNoTotem = false;
    }

    @Override
    public void onEnable() {
        this.eventManager.add(HudListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(HudListener.class, this);
        super.onDisable();
    }

    @Override
    public void onRenderHud(HudListener.HudEvent event) {
        if (this.mc.field_1724 == null) {
            return;
        }
        if (Argon.INSTANCE.getModuleManager().getModule(AutoCrystal.class).crystalling && this.stopOnCrystal.getValue()) {
            return;
        }
        double squaredDistance = this.distance.getValue() * this.distance.getValue();
        class_1661 inventory = this.mc.field_1724.method_31548();
        if (this.checkShield.getValue() && this.mc.field_1724.method_6039()) {
            return;
        }
        if (((class_1799)inventory.field_7544.get(0)).method_7909() != class_1802.field_8288 && this.onPop.getValue() && !this.offhandHasNoTotem) {
            this.offhandHasNoTotem = true;
            InventoryUtils.selectItemFromHotbar(class_1802.field_8288);
        }
        if (((class_1799)inventory.field_7544.get(0)).method_7909() == class_1802.field_8288) {
            this.offhandHasNoTotem = false;
        }
        if ((double)this.mc.field_1724.method_6032() <= this.health.getValue() && this.onHealth.getValue() && !this.belowHealth) {
            this.belowHealth = true;
            InventoryUtils.selectItemFromHotbar(class_1802.field_8288);
        }
        if ((double)this.mc.field_1724.method_6032() > this.health.getValue()) {
            this.belowHealth = false;
        }
        if (!this.predict.getValue()) {
            return;
        }
        if (this.mc.field_1724.method_6032() > 19.0f) {
            return;
        }
        if (!this.onGround.getValue() && this.mc.field_1724.method_24828()) {
            return;
        }
        if (this.checkPlayers.getValue() && this.mc.field_1687.method_18456().parallelStream().filter(e -> e != this.mc.field_1724).noneMatch(p -> this.mc.field_1724.method_5858((class_1297)p) <= squaredDistance)) {
            return;
        }
        double above = this.activatesAbove.getValue();
        int floor = (int)Math.floor(above);
        for (int i = 1; i <= floor; ++i) {
            if (this.mc.field_1687.method_8320(this.mc.field_1724.method_24515().method_10069(0, -i, 0)).method_26215()) continue;
            return;
        }
        class_243 playerPos = this.mc.field_1724.method_19538();
        class_2338 playerBlockPos = new class_2338((int)playerPos.field_1352, (int)playerPos.field_1351 - (int)above, (int)playerPos.field_1350);
        if (!this.mc.field_1687.method_8320(new class_2338((class_2382)playerBlockPos)).method_26215()) {
            return;
        }
        List<class_1511> crystals = this.nearbyCrystals();
        ArrayList pos = new ArrayList();
        crystals.forEach(e -> pos.add(e.method_19538()));
        if (this.predictCrystals.getValue()) {
            Stream<class_2338> s = BlockUtils.getAllInBoxStream(this.mc.field_1724.method_24515().method_10069(-6, -8, -6), this.mc.field_1724.method_24515().method_10069(6, 2, 6)).filter(e -> this.mc.field_1687.method_8320(e).method_26204() == class_2246.field_10540 || this.mc.field_1687.method_8320(e).method_26204() == class_2246.field_9987).filter(CrystalUtils::canPlaceCrystalClient);
            if (this.checkAim.getValue()) {
                s = this.checkItems.getValue() ? s.filter(this::arePeopleAimingAtBlockAndHoldingCrystals) : s.filter(this::arePeopleAimingAtBlock);
            }
            s.forEachOrdered(e -> pos.add(class_243.method_24955((class_2382)e).method_1031(0.0, 1.0, 0.0)));
        }
        for (class_243 crys : pos) {
            double damage = DamageUtils.crystalDamage((class_1309)this.mc.field_1724, crys);
            if (!(damage >= (double)(this.mc.field_1724.method_6032() + this.mc.field_1724.method_6067()))) continue;
            InventoryUtils.selectItemFromHotbar(class_1802.field_8288);
            break;
        }
    }

    private List<class_1511> nearbyCrystals() {
        class_243 pos = this.mc.field_1724.method_19538();
        return this.mc.field_1687.method_8390(class_1511.class, new class_238(pos.method_1031(-6.0, -6.0, -6.0), pos.method_1031(6.0, 6.0, 6.0)), e -> true);
    }

    private boolean arePeopleAimingAtBlock(class_2338 block) {
        class_243[] eyesPos = new class_243[1];
        class_3965[] hitResult = new class_3965[1];
        return this.mc.field_1687.method_18456().parallelStream().filter(e -> e != this.mc.field_1724).anyMatch(e -> {
            eyesPos[0] = RotationUtils.getEyesPos((class_1657)e);
            hitResult[0] = this.mc.field_1687.method_17742(new class_3959(eyesPos[0], eyesPos[0].method_1019(RotationUtils.getPlayerLookVec((class_1657)e).method_1021(4.5)), class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)e));
            return hitResult[0] != null && hitResult[0].method_17777().equals((Object)block);
        });
    }

    private boolean arePeopleAimingAtBlockAndHoldingCrystals(class_2338 block) {
        class_243[] eyesPos = new class_243[1];
        class_3965[] hitResult = new class_3965[1];
        return this.mc.field_1687.method_18456().parallelStream().filter(e -> e != this.mc.field_1724).filter(e -> e.method_24518(class_1802.field_8301)).anyMatch(e -> {
            eyesPos[0] = RotationUtils.getEyesPos((class_1657)e);
            hitResult[0] = this.mc.field_1687.method_17742(new class_3959(eyesPos[0], eyesPos[0].method_1019(RotationUtils.getPlayerLookVec((class_1657)e).method_1021(4.5)), class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)e));
            return hitResult[0] != null && hitResult[0].method_17777().equals((Object)block);
        });
    }
}
