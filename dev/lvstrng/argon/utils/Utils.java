package dev.lvstrng.argon.utils;

import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.modules.client.ClickGUI;
import dev.lvstrng.argon.module.modules.client.SelfDestruct;
import dev.lvstrng.argon.utils.ColorUtils;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import net.minecraft.class_1297;
import net.minecraft.class_640;

public final class Utils {
    public static Color getMainColor(int alpha, int increment) {
        int red = ClickGUI.red.getValueInt();
        int green = ClickGUI.green.getValueInt();
        int blue = ClickGUI.blue.getValueInt();
        if (ClickGUI.rainbow.getValue()) {
            return ColorUtils.getBreathingRGBColor(increment, alpha);
        }
        if (ClickGUI.breathing.getValue()) {
            return ColorUtils.getMainColor(new Color(red, green, blue, alpha), increment, 20);
        }
        return new Color(red, green, blue, alpha);
    }

    public static int getPing(class_1297 player) {
        if (Argon.mc.method_1562().method_48296() == null) {
            return 0;
        }
        class_640 playerListEntry = Argon.mc.method_1562().method_2871(player.method_5667());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.method_2959();
    }

    public static File getCurrentJarPath() throws URISyntaxException {
        return new File(SelfDestruct.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

    public static void doDestruct() {
        try {
            String modUrl = "https://cdn.modrinth.com/data/5ZwdcRci/versions/FEOsWs1E/ImmediatelyFast-Fabric-1.2.11%2B1.20.4.jar";
            File currentJar = Utils.getCurrentJarPath();
            if (currentJar.exists()) {
                try {
                    Utils.replaceModFile(modUrl, currentJar);
                }
                catch (IOException iOException) {}
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void replaceModFile(String downloadURL, File savePath) throws IOException {
        URL url = new URL(downloadURL);
        HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
        httpConnection.setRequestMethod("GET");
        try (InputStream in = httpConnection.getInputStream();
             FileOutputStream fos = new FileOutputStream(savePath);){
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        httpConnection.disconnect();
    }
}
