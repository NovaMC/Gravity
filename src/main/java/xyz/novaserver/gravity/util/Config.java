package xyz.novaserver.gravity.util;

import com.google.common.io.ByteStreams;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import xyz.novaserver.gravity.Gravity;

import java.io.*;
import java.nio.file.Path;

public class Config {
    private static ConfigurationNode rootNode;

    public static boolean loadConfig(Path dataDirectory) {
        File configFile = new File(dataDirectory.toFile(), "config.yml");

        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = Gravity.getInstance().getClass().getResourceAsStream("/" + configFile.getName());
                    OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configFile.toPath()).build();

        try {
            rootNode = loader.load();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ConfigurationNode getRoot() {
        return rootNode;
    }
}
