package dev.lvstrng.argon.utils;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.utils.RenderUtils;
import dev.lvstrng.argon.utils.rotation.Rotation;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class RotationUtils {
    public static class_243 getEyesPos(class_1657 player) {
        return RenderUtils.getCameraPos();
    }

    public static class_2338 getCameraBlockPos() {
        return Argon.mc.method_31975().field_4344.method_19328();
    }

    public static class_2338 getEyesBlockPos() {
        return new class_2338((int)RotationUtils.getEyesPos((class_1657)Argon.mc.field_1724).field_1352, (int)RotationUtils.getEyesPos((class_1657)Argon.mc.field_1724).field_1351, (int)RotationUtils.getEyesPos((class_1657)Argon.mc.field_1724).field_1350);
    }

    public static class_243 getPlayerLookVec(float yaw, float pitch) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        float i = class_3532.method_15374((float)g);
        float j = class_3532.method_15362((float)f);
        float k = class_3532.method_15374((float)f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }

    public static class_243 getPlayerLookVec(class_1657 player) {
        return RotationUtils.getPlayerLookVec(player.method_36454(), player.method_36455());
    }

    public static Rotation getDiff(Rotation rotation1, Rotation rotation2) {
        double yaw = Math.abs(Math.max(rotation1.yaw(), rotation2.yaw()) - Math.min(rotation1.yaw(), rotation2.yaw()));
        double pitch = Math.abs(Math.max(rotation1.pitch(), rotation2.pitch()) - Math.min(rotation1.pitch(), rotation2.pitch()));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getSmoothRotation(Rotation from, Rotation to, double speed) {
        return new Rotation(class_3532.method_17821((float)((float)speed), (float)((float)from.yaw()), (float)((float)to.yaw())), class_3532.method_17821((float)((float)speed), (float)((float)from.pitch()), (float)((float)to.pitch())));
    }

    public static double getTotalDiff(Rotation rotation1, Rotation rotation2) {
        Rotation diff = RotationUtils.getDiff(rotation1, rotation2);
        return diff.yaw() + diff.pitch();
    }

    public static class_243 getClientLookVec() {
        return RotationUtils.getPlayerLookVec((class_1657)Argon.mc.field_1724);
    }

    public static Rotation getDirection(class_1297 entity, class_243 vec) {
        double dx = vec.field_1352 - entity.method_23317();
        double dy = vec.field_1351 - entity.method_23318();
        double dz = vec.field_1350 - entity.method_23321();
        double dist = class_3532.method_15355((float)((float)(dx * dx + dz * dz)));
        return new Rotation(class_3532.method_15338((double)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0)), -class_3532.method_15338((double)Math.toDegrees(Math.atan2(dy, dist))));
    }

    public static double getAngleToRotation(Rotation rotation) {
        double currentYaw = class_3532.method_15393((float)Argon.mc.field_1724.method_36454());
        double currentPitch = class_3532.method_15393((float)Argon.mc.field_1724.method_36455());
        double diffYaw = class_3532.method_15338((double)(currentYaw - rotation.yaw()));
        double diffPitch = class_3532.method_15338((double)(currentPitch - rotation.pitch()));
        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }
}
