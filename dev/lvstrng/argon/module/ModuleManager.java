package dev.lvstrng.argon.module;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.event.events.ButtonListener;
import dev.lvstrng.argon.module.Category;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.modules.client.Friends;
import dev.lvstrng.argon.module.modules.client.SelfDestruct;
import dev.lvstrng.argon.module.modules.combat.AimAssist;
import dev.lvstrng.argon.module.modules.combat.AnchorMacro;
import dev.lvstrng.argon.module.modules.combat.AutoCrystal;
import dev.lvstrng.argon.module.modules.combat.AutoDoubleHand;
import dev.lvstrng.argon.module.modules.combat.AutoHitCrystal;
import dev.lvstrng.argon.module.modules.combat.AutoInventoryTotem;
import dev.lvstrng.argon.module.modules.combat.AutoJumpReset;
import dev.lvstrng.argon.module.modules.combat.AutoPot;
import dev.lvstrng.argon.module.modules.combat.AutoPotRefill;
import dev.lvstrng.argon.module.modules.combat.AutoWTap;
import dev.lvstrng.argon.module.modules.combat.CrystalOptimizer;
import dev.lvstrng.argon.module.modules.combat.DoubleAnchor;
import dev.lvstrng.argon.module.modules.combat.HoverTotem;
import dev.lvstrng.argon.module.modules.combat.NoMissDelay;
import dev.lvstrng.argon.module.modules.combat.ShieldDisabler;
import dev.lvstrng.argon.module.modules.combat.TotemOffhand;
import dev.lvstrng.argon.module.modules.combat.TriggerBot;
import dev.lvstrng.argon.module.modules.misc.AutoClicker;
import dev.lvstrng.argon.module.modules.misc.AutoXP;
import dev.lvstrng.argon.module.modules.misc.FakeLag;
import dev.lvstrng.argon.module.modules.misc.Freecam;
import dev.lvstrng.argon.module.modules.misc.KeyPearl;
import dev.lvstrng.argon.module.modules.misc.NoBreakDelay;
import dev.lvstrng.argon.module.modules.misc.NoJumpDelay;
import dev.lvstrng.argon.module.modules.misc.PackSpoof;
import dev.lvstrng.argon.module.modules.misc.PingSpoof;
import dev.lvstrng.argon.module.modules.misc.Prevent;
import dev.lvstrng.argon.module.modules.misc.Sprint;
import dev.lvstrng.argon.module.modules.render.HUD;
import dev.lvstrng.argon.module.modules.render.NoBounce;
import dev.lvstrng.argon.module.modules.render.PlayerESP;
import dev.lvstrng.argon.module.modules.render.StorageEsp;
import dev.lvstrng.argon.module.modules.render.TargetHud;
import dev.lvstrng.argon.module.setting.KeybindSetting;
import dev.lvstrng.argon.module.setting.Setting;
import dev.lvstrng.argon.utils.EncryptedString;
import java.util.ArrayList;
import java.util.List;

public final class ModuleManager
implements ButtonListener {
    private final List<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        this.addModules();
        this.addKeybinds();
    }

    public void addModules() {
        this.add(new AimAssist());
        this.add(new AnchorMacro());
        this.add(new AutoCrystal());
        this.add(new AutoDoubleHand());
        this.add(new AutoHitCrystal());
        this.add(new AutoInventoryTotem());
        this.add(new TriggerBot());
        this.add(new AutoPot());
        this.add(new AutoPotRefill());
        this.add(new AutoWTap());
        this.add(new CrystalOptimizer());
        this.add(new DoubleAnchor());
        this.add(new HoverTotem());
        this.add(new NoMissDelay());
        this.add(new ShieldDisabler());
        this.add(new TotemOffhand());
        this.add(new AutoJumpReset());
        this.add(new Prevent());
        this.add(new AutoXP());
        this.add(new NoJumpDelay());
        this.add(new PingSpoof());
        this.add(new FakeLag());
        this.add(new AutoClicker());
        this.add(new KeyPearl());
        this.add(new NoBreakDelay());
        this.add(new Freecam());
        this.add(new PackSpoof());
        this.add(new Sprint());
        this.add(new HUD());
        this.add(new NoBounce());
        this.add(new PlayerESP());
        this.add(new StorageEsp());
        this.add(new TargetHud());
        this.add(new ClickGUI());
        this.add(new Friends());
        this.add(new SelfDestruct());
    }

    public List<Module> getEnabledModules() {
        return this.modules.stream().filter(Module::isEnabled).toList();
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public void addKeybinds() {
        Argon.INSTANCE.getEventManager().add(ButtonListener.class, this);
        for (Module module : this.modules) {
            module.addSetting((Setting<?>)new KeybindSetting(EncryptedString.of("Keybind"), module.getKey(), true).setDescription(EncryptedString.of("Key to enabled the module")));
        }
    }

    public List<Module> getModulesInCategory(Category category) {
        return this.modules.stream().filter(module -> module.getCategory() == category).toList();
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T)((Module)this.modules.stream().filter(moduleClass::isInstance).findFirst().orElse(null));
    }

    public void add(Module module) {
        this.modules.add(module);
    }

    @Override
    public void onButtonPress(ButtonListener.ButtonEvent event) {
        if (!SelfDestruct.destruct) {
            this.modules.forEach(module -> {
                if (module.getKey() == event.button && event.action == 1) {
                    module.toggle();
                }
            });
        }
    }
}
