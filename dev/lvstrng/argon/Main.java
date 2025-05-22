package dev.lvstrng.argon;

import dev.lvstrng.argon.Argon;
import java.io.IOException;
import net.fabricmc.api.ModInitializer;

public final class Main
implements ModInitializer {
    public void onInitialize() {
        try {
            new Argon();
        }
        catch (IOException | InterruptedException exception) {
            // empty catch block
        }
    }
}
