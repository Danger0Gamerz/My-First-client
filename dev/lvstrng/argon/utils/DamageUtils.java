package dev.lvstrng.argon.utils;

import dev.lvstrng.argon.Argon;
import java.util.function.BiFunction;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1922;
import net.minecraft.class_1934;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2902;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_640;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

public class DamageUtils {
    public static final RaycastFactory HIT_FACTORY = (context, blockPos) -> {
        class_2680 blockState = Argon.mc.field_1687.method_8320(blockPos);
        if (blockState.method_26204().method_9520() < 600.0f) {
            return null;
        }
        return blockState.method_26220((class_1922)Argon.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
    };

    private DamageUtils() {
    }

    public static float crystalDamage(class_1309 target, class_243 targetPos, class_238 targetBox, class_243 explosionPos, RaycastFactory raycastFactory) {
        return DamageUtils.explosionDamage(target, targetPos, targetBox, explosionPos, 12.0f, raycastFactory);
    }

    public static float bedDamage(class_1309 target, class_243 targetPos, class_238 targetBox, class_243 explosionPos, RaycastFactory raycastFactory) {
        return DamageUtils.explosionDamage(target, targetPos, targetBox, explosionPos, 10.0f, raycastFactory);
    }

    public static float anchorDamage(class_1309 target, class_243 targetPos, class_238 targetBox, class_243 explosionPos, RaycastFactory raycastFactory) {
        return DamageUtils.explosionDamage(target, targetPos, targetBox, explosionPos, 10.0f, raycastFactory);
    }

    public static float explosionDamage(class_1309 target, class_243 targetPos, class_238 targetBox, class_243 explosionPos, float power, RaycastFactory raycastFactory) {
        double modDistance = DamageUtils.distance(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350, explosionPos.field_1352, explosionPos.field_1351, explosionPos.field_1350);
        if (modDistance > (double)power) {
            return 0.0f;
        }
        double exposure = DamageUtils.getExposure(explosionPos, targetBox, raycastFactory);
        double impact = (1.0 - modDistance / (double)power) * exposure;
        float damage = (int)((impact * impact + impact) / 2.0 * 7.0 * 12.0 + 1.0);
        return DamageUtils.calculateReductions(damage, target, Argon.mc.field_1687.method_48963().method_48807(null));
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return java.lang.Math.sqrt(DamageUtils.squaredDistance(x1, y1, z1, x2, y2, z2));
    }

    public static double distanceTo(class_1297 entity) {
        return DamageUtils.distanceTo(entity.method_23317(), entity.method_23318(), entity.method_23321());
    }

    public static double distanceTo(class_2338 blockPos) {
        return DamageUtils.distanceTo(blockPos.method_10263(), blockPos.method_10264(), blockPos.method_10260());
    }

    public static double distanceTo(class_243 vec3d) {
        return DamageUtils.distanceTo(vec3d.method_10216(), vec3d.method_10214(), vec3d.method_10215());
    }

    public static double distanceTo(double x, double y, double z) {
        return java.lang.Math.sqrt(DamageUtils.squaredDistanceTo(x, y, z));
    }

    public static double squaredDistanceTo(class_1297 entity) {
        return DamageUtils.squaredDistanceTo(entity.method_23317(), entity.method_23318(), entity.method_23321());
    }

    public static double squaredDistanceTo(class_2338 blockPos) {
        return DamageUtils.squaredDistanceTo(blockPos.method_10263(), blockPos.method_10264(), blockPos.method_10260());
    }

    public static double squaredDistanceTo(double x, double y, double z) {
        return DamageUtils.squaredDistance(Argon.mc.field_1724.method_23317(), Argon.mc.field_1724.method_23318(), Argon.mc.field_1724.method_23321(), x, y, z);
    }

    public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double f = x1 - x2;
        double g = y1 - y2;
        double h = z1 - z2;
        return Math.fma((double)f, (double)f, (double)Math.fma((double)g, (double)g, (double)(h * h)));
    }

    public static float crystalDamage(class_1309 target, class_243 crystal, boolean predictMovement, class_2338 obsidianPos) {
        return DamageUtils.overridingExplosionDamage(target, crystal, 12.0f, predictMovement, obsidianPos, class_2246.field_10540.method_9564());
    }

    public static float crystalDamage(class_1309 target, class_243 crystal) {
        return DamageUtils.explosionDamage(target, crystal, 12.0f, false);
    }

    public static float bedDamage(class_1309 target, class_243 bed) {
        return DamageUtils.explosionDamage(target, bed, 10.0f, false);
    }

    public static float anchorDamage(class_1309 target, class_243 anchor) {
        return DamageUtils.overridingExplosionDamage(target, anchor, 10.0f, false, class_2338.method_49638((class_2374)anchor), class_2246.field_10124.method_9564());
    }

    private static float overridingExplosionDamage(class_1309 target, class_243 explosionPos, float power, boolean predictMovement, class_2338 overridePos, class_2680 overrideState) {
        return DamageUtils.explosionDamage(target, explosionPos, power, predictMovement, DamageUtils.getOverridingHitFactory(overridePos, overrideState));
    }

    private static float explosionDamage(class_1309 target, class_243 explosionPos, float power, boolean predictMovement) {
        return DamageUtils.explosionDamage(target, explosionPos, power, predictMovement, HIT_FACTORY);
    }

    public static class_1934 getGameMode(class_1657 player) {
        class_640 playerListEntry = Argon.mc.method_1562().method_2871(player.method_5667());
        if (playerListEntry == null) {
            return class_1934.field_9219;
        }
        return playerListEntry.method_2958();
    }

    private static float explosionDamage(class_1309 target, class_243 explosionPos, float power, boolean predictMovement, RaycastFactory raycastFactory) {
        class_1657 player;
        if (target == null) {
            return 0.0f;
        }
        if (target instanceof class_1657 && DamageUtils.getGameMode(player = (class_1657)target) == class_1934.field_9220) {
            return 0.0f;
        }
        class_243 position = predictMovement ? target.method_19538().method_1019(target.method_18798()) : target.method_19538();
        class_238 box = target.method_5829();
        if (predictMovement) {
            box = box.method_997(target.method_18798());
        }
        return DamageUtils.explosionDamage(target, position, box, explosionPos, power, raycastFactory);
    }

    public static RaycastFactory getOverridingHitFactory(class_2338 overridePos, class_2680 overrideState) {
        return (context, blockPos) -> {
            class_2680 blockState;
            if (blockPos.equals((Object)overridePos)) {
                blockState = overrideState;
            } else {
                blockState = Argon.mc.field_1687.method_8320(blockPos);
                if (blockState.method_26204().method_9520() < 600.0f) {
                    return null;
                }
            }
            return blockState.method_26220((class_1922)Argon.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
        };
    }

    public static float getAttackDamage(class_1309 attacker, class_1309 target) {
        class_1282 class_12822;
        float itemDamage = (float)attacker.method_45325(class_5134.field_23721);
        if (attacker instanceof class_1657) {
            class_1657 player = (class_1657)attacker;
            class_12822 = Argon.mc.field_1687.method_48963().method_48802(player);
        } else {
            class_12822 = Argon.mc.field_1687.method_48963().method_48812(attacker);
        }
        class_1282 damageSource = class_12822;
        class_1799 stack = attacker.method_59958();
        float enchantDamage = 0.0f;
        if (attacker instanceof class_1657) {
            class_1657 playerEntity = (class_1657)attacker;
            float charge = playerEntity.method_7261(0.5f);
            itemDamage *= 0.2f + charge * charge * 0.8f;
            enchantDamage *= charge;
            if (!(!(charge > 0.9f) || !(attacker.field_6017 > 0.0f) || attacker.method_24828() || attacker.method_6101() || attacker.method_5799() || attacker.method_6059(class_1294.field_5919) || attacker.method_5765())) {
                itemDamage *= 1.5f;
            }
        }
        float damage = itemDamage + enchantDamage;
        damage = DamageUtils.calculateReductions(damage, target, damageSource);
        return damage;
    }

    public static float fallDamage(class_1309 entity) {
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            if (player.method_31549().field_7479) {
                return 0.0f;
            }
        }
        if (entity.method_6059(class_1294.field_5906) || entity.method_6059(class_1294.field_5902)) {
            return 0.0f;
        }
        int surface = Argon.mc.field_1687.method_8500(entity.method_24515()).method_12032(class_2902.class_2903.field_13197).method_12603(entity.method_31477() & 0xF, entity.method_31479() & 0xF);
        if (entity.method_31478() >= surface) {
            return DamageUtils.fallDamageReductions(entity, surface);
        }
        class_3965 raycastResult = Argon.mc.field_1687.method_17742(new class_3959(entity.method_19538(), new class_243(entity.method_23317(), (double)Argon.mc.field_1687.method_31607(), entity.method_23321()), class_3959.class_3960.field_17558, class_3959.class_242.field_36338, (class_1297)entity));
        if (raycastResult.method_17783() == class_239.class_240.field_1333) {
            return 0.0f;
        }
        return DamageUtils.fallDamageReductions(entity, raycastResult.method_17777().method_10264());
    }

    private static float fallDamageReductions(class_1309 entity, int surface) {
        int fallHeight = (int)(entity.method_23318() - (double)surface + (double)entity.field_6017 - 3.0);
        @Nullable class_1293 jumpBoostInstance = entity.method_6112(class_1294.field_5913);
        if (jumpBoostInstance != null) {
            fallHeight -= jumpBoostInstance.method_5578() + 1;
        }
        return DamageUtils.calculateReductions(fallHeight, entity, Argon.mc.field_1687.method_48963().method_48827());
    }

    public static float calculateReductions(float damage, class_1309 entity, class_1282 damageSource) {
        if (damageSource.method_5514()) {
            switch (Argon.mc.field_1687.method_8407()) {
                case field_5805: {
                    damage = java.lang.Math.min(damage / 2.0f + 1.0f, damage);
                    break;
                }
                case field_5807: {
                    damage *= 1.5f;
                }
            }
        }
        damage = class_1280.method_5496((class_1309)entity, (float)damage, (class_1282)damageSource, (float)DamageUtils.getArmor(entity), (float)((float)entity.method_45325(class_5134.field_23725)));
        damage = DamageUtils.resistanceReduction(entity, damage);
        damage = DamageUtils.protectionReduction(entity, damage, damageSource);
        return java.lang.Math.max(damage, 0.0f);
    }

    private static float getArmor(class_1309 entity) {
        return (float)java.lang.Math.floor(entity.method_45325(class_5134.field_23724));
    }

    private static float protectionReduction(class_1309 player, float damage, class_1282 source) {
        return class_1280.method_5497((float)damage, (float)0.0f);
    }

    private static float resistanceReduction(class_1309 player, float damage) {
        class_1293 resistance = player.method_6112(class_1294.field_5907);
        if (resistance != null) {
            int lvl = resistance.method_5578() + 1;
            damage *= 1.0f - (float)lvl * 0.2f;
        }
        return java.lang.Math.max(damage, 0.0f);
    }

    private static float getExposure(class_243 source, class_238 box, RaycastFactory raycastFactory) {
        double xDiff = box.field_1320 - box.field_1323;
        double yDiff = box.field_1325 - box.field_1322;
        double zDiff = box.field_1324 - box.field_1321;
        double xStep = 1.0 / (xDiff * 2.0 + 1.0);
        double yStep = 1.0 / (yDiff * 2.0 + 1.0);
        double zStep = 1.0 / (zDiff * 2.0 + 1.0);
        if (xStep > 0.0 && yStep > 0.0 && zStep > 0.0) {
            int misses = 0;
            int hits = 0;
            double xOffset = (1.0 - java.lang.Math.floor(1.0 / xStep) * xStep) * 0.5;
            double zOffset = (1.0 - java.lang.Math.floor(1.0 / zStep) * zStep) * 0.5;
            xStep *= xDiff;
            yStep *= yDiff;
            zStep *= zDiff;
            double startX = box.field_1323 + xOffset;
            double startY = box.field_1322;
            double startZ = box.field_1321 + zOffset;
            double endX = box.field_1320 + xOffset;
            double endY = box.field_1325;
            double endZ = box.field_1324 + zOffset;
            for (double x = startX; x <= endX; x += xStep) {
                for (double y = startY; y <= endY; y += yStep) {
                    for (double z = startZ; z <= endZ; z += zStep) {
                        class_243 position = new class_243(x, y, z);
                        if (DamageUtils.raycast(new ExposureRaycastContext(position, source), raycastFactory) == null) {
                            ++misses;
                        }
                        ++hits;
                    }
                }
            }
            return (float)misses / (float)hits;
        }
        return 0.0f;
    }

    private static class_3965 raycast(ExposureRaycastContext context, RaycastFactory raycastFactory) {
        return (class_3965)class_1922.method_17744((class_243)context.start, (class_243)context.end, (Object)context, (BiFunction)raycastFactory, ctx -> null);
    }

    @FunctionalInterface
    public static interface RaycastFactory
    extends BiFunction<ExposureRaycastContext, class_2338, class_3965> {
    }

    public record ExposureRaycastContext(class_243 start, class_243 end) {
    }
}
