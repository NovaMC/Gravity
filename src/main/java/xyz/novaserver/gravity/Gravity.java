package xyz.novaserver.gravity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import xyz.novaserver.gravity.command.*;
import xyz.novaserver.gravity.listener.BedrockJoinListener;
import xyz.novaserver.gravity.queue.QueueHandler;
import xyz.novaserver.gravity.util.Config;

import java.nio.file.Path;

@Plugin(id = "gravity",
        name = "Gravity",
        version = "0.1.1",
        authors = {"Lui798"},
        dependencies = {
                @Dependency(id = "floodgate", optional = true)
        })
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

        ConfigurationNode config = Config.getRoot();

        if (config.getNode("discord").getNode("enabled").getBoolean()) {
            proxy.getCommandManager().register("discord", new DiscordCommand());
        }
        if (config.getNode("map").getNode("enabled").getBoolean()) {
            proxy.getCommandManager().register("map", new MapCommand());
        }
        if (config.getNode("report").getNode("enabled").getBoolean()) {
            proxy.getCommandManager().register("report", new ReportCommand());
        }
        if (config.getNode("hub").getNode("enabled").getBoolean()) {
            proxy.getCommandManager().register("hub", new HubCommand(), "lobby");
        }
        if (config.getNode("store").getNode("enabled").getBoolean()) {
            proxy.getCommandManager().register("store", new StoreCommand(), "donate");
        }

        if (config.getNode("bedrock").getNode("enabled").getBoolean()
                && getProxy().getPluginManager().isLoaded("floodgate")) {
            getProxy().getEventManager().register(this, new BedrockJoinListener());
        }
        if (config.getNode("rejoin").getNode("enabled").getBoolean()) {
            new QueueHandler(this);
        }
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public static Gravity getInstance() {
        return gravity;
    }
}
