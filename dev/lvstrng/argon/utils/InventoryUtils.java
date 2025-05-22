package dev.lvstrng.argon.utils;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.mixin.ClientPlayerInteractionManagerAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1661;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1828;
import net.minecraft.class_1829;
import net.minecraft.class_1844;
import net.minecraft.class_7923;
import net.minecraft.class_9334;

public final class InventoryUtils {
    public static void setInvSlot(int slot) {
        Argon.mc.field_1724.method_31548().field_7545 = slot;
        ((ClientPlayerInteractionManagerAccessor)Argon.mc.field_1761).syncSlot();
    }

    public static boolean selectItemFromHotbar(Predicate<class_1792> item) {
        class_1661 inv = Argon.mc.field_1724.method_31548();
        for (int i = 0; i < 9; ++i) {
            class_1799 itemStack = inv.method_5438(i);
            if (!item.test(itemStack.method_7909())) continue;
            inv.field_7545 = i;
            return true;
        }
        return false;
    }

    public static boolean selectItemFromHotbar(class_1792 item) {
        return InventoryUtils.selectItemFromHotbar((class_1792 i) -> i == item);
    }

    public static boolean hasItemInHotbar(Predicate<class_1792> item) {
        class_1661 inv = Argon.mc.field_1724.method_31548();
        for (int i = 0; i < 9; ++i) {
            class_1799 itemStack = inv.method_5438(i);
            if (!item.test(itemStack.method_7909())) continue;
            return true;
        }
        return false;
    }

    public static int countItem(Predicate<class_1792> item) {
        class_1661 inv = Argon.mc.field_1724.method_31548();
        int count = 0;
        for (int i = 0; i < 36; ++i) {
            class_1799 itemStack = inv.method_5438(i);
            if (!item.test(itemStack.method_7909())) continue;
            count += itemStack.method_7947();
        }
        return count;
    }

    public static int countItemExceptHotbar(Predicate<class_1792> item) {
        class_1661 inv = Argon.mc.field_1724.method_31548();
        int count = 0;
        for (int i = 9; i < 36; ++i) {
            class_1799 itemStack = inv.method_5438(i);
            if (!item.test(itemStack.method_7909())) continue;
            count += itemStack.method_7947();
        }
        return count;
    }

    public static int getSwordSlot() {
        class_1661 playerInventory = Argon.mc.field_1724.method_31548();
        for (int itemIndex = 0; itemIndex < 9; ++itemIndex) {
            if (!(playerInventory.method_5438(itemIndex).method_7909() instanceof class_1829)) continue;
            return itemIndex;
        }
        return -1;
    }

    public static boolean selectSword() {
        int itemIndex = InventoryUtils.getSwordSlot();
        if (itemIndex != -1) {
            InventoryUtils.setInvSlot(itemIndex);
            return true;
        }
        return false;
    }

    public static int findSplash(class_1291 type, int duration, int amplifier) {
        class_1661 inv = Argon.mc.field_1724.method_31548();
        class_1293 potion = new class_1293(class_7923.field_41174.method_47983((Object)type), duration, amplifier);
        for (int i = 0; i < 9; ++i) {
            String s;
            class_1799 itemStack = inv.method_5438(i);
            if (!(itemStack.method_7909() instanceof class_1828) || !(s = ((class_1844)itemStack.method_57824(class_9334.field_49651)).method_57397().toString()).contains(potion.toString())) continue;
            return i;
        }
        return -1;
    }

    public static boolean isThatSplash(class_1291 type, int duration, int amplifier, class_1799 itemStack) {
        class_1293 potion = new class_1293(class_7923.field_41174.method_47983((Object)type), duration, amplifier);
        return itemStack.method_7909() instanceof class_1828 && ((class_1844)itemStack.method_57824(class_9334.field_49651)).method_57397().toString().contains(potion.toString());
    }

    public static int findTotemSlot() {
        assert (Argon.mc.field_1724 != null);
        class_1661 inv = Argon.mc.field_1724.method_31548();
        for (int index = 9; index < 36; ++index) {
            if (((class_1799)inv.field_7547.get(index)).method_7909() != class_1802.field_8288) continue;
            return index;
        }
        return -1;
    }

    public static boolean selectAxe() {
        int itemIndex = InventoryUtils.getAxeSlot();
        if (itemIndex != -1) {
            Argon.mc.field_1724.method_31548().field_7545 = itemIndex;
            return true;
        }
        return false;
    }

    public static int findRandomTotemSlot() {
        class_1661 inventory = Argon.mc.field_1724.method_31548();
        Random random = new Random();
        ArrayList<Integer> totemIndexes = new ArrayList<Integer>();
        for (int i = 9; i < 36; ++i) {
            if (((class_1799)inventory.field_7547.get(i)).method_7909() != class_1802.field_8288) continue;
            totemIndexes.add(i);
        }
        if (!totemIndexes.isEmpty()) {
            return (Integer)totemIndexes.get(random.nextInt(totemIndexes.size()));
        }
        return -1;
    }

    public static int findRandomPot(String potion) {
        class_1661 inventory = Argon.mc.field_1724.method_31548();
        Random random = new Random();
        int slotIndex = random.nextInt(27) + 9;
        for (int i = 0; i < 27; ++i) {
            int index = (slotIndex + i) % 36;
            class_1799 itemStack = (class_1799)inventory.field_7547.get(index);
            if (!(itemStack.method_7909() instanceof class_1828) || index == 36 && index == 37 && index == 38 && index == 39) continue;
            if (!((class_1844)itemStack.method_57824(class_9334.field_49651)).method_57397().toString().contains(potion.toString())) {
                return -1;
            }
            return index;
        }
        return -1;
    }

    public static int findPot(class_1291 effect, int duration, int amplifier) {
        assert (Argon.mc.field_1724 != null);
        class_1661 inv = Argon.mc.field_1724.method_31548();
        class_1293 instance = new class_1293(class_7923.field_41174.method_47983((Object)effect), duration, amplifier);
        for (int index = 9; index < 34; ++index) {
            if (!(((class_1799)inv.field_7547.get(index)).method_7909() instanceof class_1828) || !((class_1844)((class_1799)inv.field_7547.get(index)).method_57824(class_9334.field_49651)).method_57397().toString().contains(instance.toString())) continue;
            return index;
        }
        return -1;
    }

    public static List<Integer> getEmptyHotbarSlots() {
        class_1661 inventory = Argon.mc.field_1724.method_31548();
        ArrayList<Integer> slots = new ArrayList<Integer>();
        for (int i = 0; i < 9; ++i) {
            if (((class_1799)inventory.field_7547.get(i)).method_7960()) {
                slots.add(i);
                continue;
            }
            if (!slots.contains(i) || ((class_1799)inventory.field_7547.get(i)).method_7960()) continue;
            slots.remove(i);
        }
        return slots;
    }

    public static int getAxeSlot() {
        class_1661 playerInventory = Argon.mc.field_1724.method_31548();
        for (int itemIndex = 0; itemIndex < 9; ++itemIndex) {
            if (!(playerInventory.method_5438(itemIndex).method_7909() instanceof class_1743)) continue;
            return itemIndex;
        }
        return -1;
    }

    public static int countItem(class_1792 item) {
        return InventoryUtils.countItem((class_1792 i) -> i == item);
    }
}
