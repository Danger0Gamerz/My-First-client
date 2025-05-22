package dev.lvstrng.argon.module.modules.combat;

import dev.lvstrng.argon.event.events.HudListener;
import dev.lvstrng.argon.event.events.MouseMoveListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.BooleanSetting;
import dev.lvstrng.argon.module.setting.MinMaxSetting;
import dev.lvstrng.argon.module.setting.ModeSetting;
import dev.lvstrng.argon.module.setting.NumberSetting;
import dev.lvstrng.argon.utils.EncryptedString;
import dev.lvstrng.argon.utils.MathUtils;
import dev.lvstrng.argon.utils.RotationUtils;
import dev.lvstrng.argon.utils.TimerUtils;
import dev.lvstrng.argon.utils.WorldUtils;
import dev.lvstrng.argon.utils.rotation.Rotation;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1743;
import net.minecraft.class_1829;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_9779;
import org.lwjgl.glfw.GLFW;

public final class AimAssist
extends Module
implements HudListener,
MouseMoveListener {
    private final BooleanSetting stickyAim = (BooleanSetting)new BooleanSetting(EncryptedString.of("Sticky Aim"), false).setDescription(EncryptedString.of("Aims at the last attacked player"));
    private final BooleanSetting onlyWeapon = new BooleanSetting(EncryptedString.of("Only Weapon"), true);
    private final BooleanSetting onLeftClick = (BooleanSetting)new BooleanSetting(EncryptedString.of("On Left Click"), false).setDescription(EncryptedString.of("Only gets triggered if holding down left click"));
    private final ModeSetting<AimMode> aimAt = new ModeSetting<AimMode>(EncryptedString.of("Aim At"), AimMode.Head, AimMode.class);
    private final BooleanSetting stopAtTargetVertical = (BooleanSetting)new BooleanSetting(EncryptedString.of("Stop at Target Vert"), true).setDescription(EncryptedString.of("Stops vertically assisting if already aiming at the entity, helps bypass anti-cheat"));
    private final BooleanSetting stopAtTargetHorizontal = (BooleanSetting)new BooleanSetting(EncryptedString.of("Stop at Target Horiz"), false).setDescription(EncryptedString.of("Stops horizontally assisting if already aiming at the entity, helps bypass anti-cheat"));
    private final NumberSetting radius = new NumberSetting(EncryptedString.of("Radius"), 0.1, 6.0, 5.0, 0.1);
    private final BooleanSetting seeOnly = new BooleanSetting(EncryptedString.of("See Only"), true);
    private final BooleanSetting lookAtNearest = new BooleanSetting(EncryptedString.of("Look at Nearest"), false);
    private final NumberSetting fov = new NumberSetting(EncryptedString.of("FOV"), 5.0, 360.0, 180.0, 1.0);
    private final MinMaxSetting pitchSpeed = new MinMaxSetting(EncryptedString.of("Vertical Speed"), 0.0, 10.0, 0.1, 2.0, 4.0);
    private final MinMaxSetting yawSpeed = new MinMaxSetting(EncryptedString.of("Horizontal Speed"), 0.0, 10.0, 0.1, 2.0, 4.0);
    private final NumberSetting speedChange = (NumberSetting)new NumberSetting(EncryptedString.of("Speed Delay"), 0.0, 1000.0, 250.0, 1.0).setDescription(EncryptedString.of("Time in milliseconds to wait after resetting random speed"));
    private final NumberSetting randomization = new NumberSetting(EncryptedString.of("Chance"), 0.0, 100.0, 50.0, 1.0);
    private final BooleanSetting yawAssist = new BooleanSetting(EncryptedString.of("Horizontal"), true);
    private final BooleanSetting pitchAssist = new BooleanSetting(EncryptedString.of("Vertical"), true);
    private final NumberSetting waitFor = (NumberSetting)new NumberSetting(EncryptedString.of("Wait on Move"), 0.0, 1000.0, 0.0, 1.0).setDescription(EncryptedString.of("After you move your mouse aim assist will stop working for the selected amount of time"));
    private final ModeSetting<LerpMode> lerp = (ModeSetting)new ModeSetting<LerpMode>(EncryptedString.of("Lerp"), LerpMode.Normal, LerpMode.class).setDescription(EncryptedString.of("Linear interpolation to use to rotate"));
    private final ModeSetting<PosMode> posMode = (ModeSetting)new ModeSetting<PosMode>(EncryptedString.of("Pos mode"), PosMode.Normal, PosMode.class).setDescription(EncryptedString.of("Precision of the target position"));
    private final TimerUtils timer = new TimerUtils();
    private final TimerUtils resetSpeed = new TimerUtils();
    private boolean move;
    private float pitch;
    private float yaw;

    public AimAssist() {
        super(EncryptedString.of("Aim Assist"), EncryptedString.of("Automatically aims at players for you"), -1, Category.COMBAT);
        this.addSettings(this.stickyAim, this.onlyWeapon, this.onLeftClick, this.aimAt, this.stopAtTargetVertical, this.stopAtTargetHorizontal, this.radius, this.seeOnly, this.lookAtNearest, this.fov, this.pitchSpeed, this.yawSpeed, this.speedChange, this.randomization, this.yawAssist, this.pitchAssist, this.waitFor, this.lerp, this.posMode);
    }

    @Override
    public void onEnable() {
        this.move = true;
        this.pitch = this.pitchSpeed.getRandomValueFloat();
        this.yaw = this.yawSpeed.getRandomValueFloat();
        this.eventManager.add(HudListener.class, this);
        this.eventManager.add(MouseMoveListener.class, this);
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(HudListener.class, this);
        this.eventManager.remove(MouseMoveListener.class, this);
        super.onDisable();
    }

    @Override
    public void onRenderHud(HudListener.HudEvent event) {
        Rotation rotation;
        double angleToRotation;
        class_243 targetPos;
        class_1657 player;
        class_1309 class_13092;
        if (this.timer.delay(this.waitFor.getValueFloat()) && !this.move) {
            this.move = true;
            this.timer.reset();
        }
        if (this.mc.field_1724 == null || this.mc.field_1755 != null) {
            return;
        }
        if (this.onlyWeapon.getValue() && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1829) && !(this.mc.field_1724.method_6047().method_7909() instanceof class_1743)) {
            return;
        }
        if (this.onLeftClick.getValue() && GLFW.glfwGetMouseButton((long)this.mc.method_22683().method_4490(), (int)0) != 1) {
            return;
        }
        class_1657 target = WorldUtils.findNearestPlayer((class_1657)this.mc.field_1724, this.radius.getValueFloat(), this.seeOnly.getValue(), true);
        if (this.stickyAim.getValue() && (class_13092 = this.mc.field_1724.method_6052()) instanceof class_1657 && (double)(player = (class_1657)class_13092).method_5739((class_1297)this.mc.field_1724) < this.radius.getValue()) {
            target = player;
        }
        if (target == null) {
            return;
        }
        if (this.resetSpeed.delay(this.speedChange.getValueFloat())) {
            this.pitch = this.pitchSpeed.getRandomValueFloat();
            this.yaw = this.yawSpeed.getRandomValueFloat();
            this.resetSpeed.reset();
        }
        class_243 class_2432 = targetPos = this.posMode.isMode(PosMode.Normal) ? target.method_19538() : target.method_30950(class_9779.field_51956.method_60637(true));
        if (this.aimAt.isMode(AimMode.Chest)) {
            targetPos = targetPos.method_1031(0.0, -0.5, 0.0);
        } else if (this.aimAt.isMode(AimMode.Legs)) {
            targetPos = targetPos.method_1031(0.0, -1.2, 0.0);
        }
        if (this.lookAtNearest.getValue()) {
            double offsetX = this.mc.field_1724.method_23317() - target.method_23317() > 0.0 ? 0.29 : -0.29;
            double offsetZ = this.mc.field_1724.method_23321() - target.method_23321() > 0.0 ? 0.29 : -0.29;
            targetPos = targetPos.method_1031(offsetX, 0.0, offsetZ);
        }
        if ((angleToRotation = RotationUtils.getAngleToRotation(rotation = RotationUtils.getDirection((class_1297)this.mc.field_1724, targetPos))) > (double)this.fov.getValueInt() / 2.0) {
            return;
        }
        float yawStrength = this.yaw / 50.0f;
        float pitchStrength = this.pitch / 50.0f;
        float yaw = this.mc.field_1724.method_36454();
        float pitch = this.mc.field_1724.method_36455();
        if (this.lerp.isMode(LerpMode.Smoothstep)) {
            yaw = (float)this.smoothStepLerp(yawStrength, this.mc.field_1724.method_36454(), (float)rotation.yaw());
            pitch = (float)this.smoothStepLerp(pitchStrength, this.mc.field_1724.method_36455(), (float)rotation.pitch());
        }
        if (this.lerp.isMode(LerpMode.Normal)) {
            yaw = this.lerp(yawStrength, this.mc.field_1724.method_36454(), (float)rotation.yaw());
            pitch = this.lerp(pitchStrength, this.mc.field_1724.method_36455(), (float)rotation.pitch());
        }
        if (this.lerp.isMode(LerpMode.EaseOut)) {
            yaw = (float)AimAssist.easeOutBackDegrees(this.mc.field_1724.method_36454(), rotation.yaw(), yawStrength * class_9779.field_51956.method_60636());
            pitch = (float)AimAssist.easeOutBackDegrees(this.mc.field_1724.method_36455(), rotation.pitch(), pitchStrength * class_9779.field_51956.method_60636());
        }
        if (MathUtils.randomInt(1, 100) <= this.randomization.getValueInt() && this.move) {
            class_3966 hitResult;
            class_239 class_2392;
            if (this.yawAssist.getValue()) {
                if (this.stopAtTargetHorizontal.getValue() && (class_2392 = WorldUtils.getHitResult(this.radius.getValue())) instanceof class_3966 && (hitResult = (class_3966)class_2392).method_17782() == target) {
                    return;
                }
                this.mc.field_1724.method_36456(yaw);
            }
            if (this.pitchAssist.getValue()) {
                if (this.stopAtTargetVertical.getValue() && (class_2392 = WorldUtils.getHitResult(this.radius.getValue())) instanceof class_3966 && (hitResult = (class_3966)class_2392).method_17782() == target) {
                    return;
                }
                this.mc.field_1724.method_36457(pitch);
            }
        }
    }

    public float lerp(float delta, float start, float end) {
        return start + class_3532.method_15393((float)(end - start)) * delta;
    }

    public static double easeOutBackDegrees(double start, double end, float speed) {
        double c1 = 1.70158;
        double c3 = 2.70158;
        double x = 1.0 - Math.pow(1.0 - (double)speed, 3.0);
        return start + class_3532.method_15338((double)(end - start)) * (1.0 + c3 * Math.pow(x - 1.0, 3.0) + c1 * Math.pow(x - 1.0, 2.0));
    }

    public double smoothStepLerp(double delta, double start, double end) {
        delta = Math.max(0.0, Math.min(1.0, delta));
        double t = delta * delta * (3.0 - 2.0 * delta);
        double value = start + class_3532.method_15338((double)(end - start)) * t;
        return value;
    }

    @Override
    public void onMouseMove(MouseMoveListener.MouseMoveEvent event) {
        this.move = false;
        this.timer.reset();
    }

    public static enum AimMode {
        Head,
        Chest,
        Legs;

    }

    public static enum LerpMode {
        Normal,
        Smoothstep,
        EaseOut;

    }

    public static enum PosMode {
        Normal,
        Lerped;

    }
}
