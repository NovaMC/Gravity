package xyz.novaserver.gravity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import xyz.novaserver.gravity.command.*;
import xyz.novaserver.gravity.util.Config;

import java.nio.file.Path;

@Plugin(id = "gravity", name = "Gravity", version = "0.1.0", authors = {"Lui798"})
public class Gravity {
    private static Gravity gravity;

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public Gravity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        gravity = this;

        this.proxy = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (!Config.loadConfig(dataDirectory)) {
            logger.error("Failed to load Gravity config file!");
        }

        proxy.getCommandManager().register("discord", new DiscordCommand());
        proxy.getCommandManager().register("map", new MapCommand());
        proxy.getCommandManager().register("privacy", new PrivacyCommand());
        proxy.getCommandManager().register("report", new ReportCommand());
        proxy.getCommandManager().register("hub", new HubCommand(), "lobby");
        proxy.getCommandManager().register("store", new StoreCommand(), "donate");
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Logger getLogger() {
        return logger;
    }

    public static Gravity getInstance() {
        return gravity;
    }
}
