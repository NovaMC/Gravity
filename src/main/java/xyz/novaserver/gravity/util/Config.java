package xyz.novaserver.gravity.util;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.novaserver.gravity.Gravity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Configuration config;

    public static void loadConfig() {
        if (!Gravity.getGravity().getDataFolder().exists()) {
            Gravity.getGravity().getDataFolder().mkdir();
        }

        File configFile = new File(Gravity.getGravity().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = Gravity.getGravity().getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            }

            catch (IOException e) {
                throw new RuntimeException("Unable to create the configuration file", e);
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(Gravity.getGravity().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getBoolean(String location) {
        return config.getBoolean(location, false);
    }

    public static String getString(String location) {
        return config.getString(location, "");
    }

    public static List<String> getStringList(String location) {
        return config.getStringList(location);
    }

    public static int getInteger(String location) {
        return config.getInt(location, 0);
    }

    public static double getDouble(String location) {
        return config.getDouble(location, 0.0);
    }

    public static String getColoredString(String location) {
        return ChatColor.translateAlternateColorCodes('&', getString(location));
    }

    public static List<String> getColoredStringList(String location) {
        List<String> list = new ArrayList<>();
        for (String s : getStringList(location)) {
            list.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return list;
    }
}
