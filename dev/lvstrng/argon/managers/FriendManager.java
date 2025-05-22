package dev.lvstrng.argon.managers;

import dev.lvstrng.argon.Argon;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_3966;

public final class FriendManager {
    private final Set<String> friends = new HashSet<String>();

    public void addFriend(class_1657 player) {
        this.friends.add(player.method_5477().getString());
    }

    public void removeFriend(class_1657 player) {
        this.friends.remove(player.method_5477().getString());
    }

    public boolean isFriend(class_1657 player) {
        return this.friends.contains(player.method_5477().getString());
    }

    public boolean isAimingOverFriend() {
        class_3966 hitResult;
        class_1297 entity;
        class_239 class_2392 = Argon.mc.field_1765;
        if (class_2392 instanceof class_3966 && (entity = (hitResult = (class_3966)class_2392).method_17782()) instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            return this.isFriend(player);
        }
        return false;
    }
}
