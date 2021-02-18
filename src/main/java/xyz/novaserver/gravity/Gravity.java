package xyz.novaserver.gravity;

import net.md_5.bungee.api.plugin.Plugin;
import xyz.novaserver.gravity.command.*;
import xyz.novaserver.gravity.listener.ProtocolChangeListener;
import xyz.novaserver.gravity.listener.ServerMoveListener;
import xyz.novaserver.gravity.util.Config;

public class Gravity extends Plugin {
    private static Gravity gravity;

    public void onEnable() {
        gravity = this;
        Config.loadConfig();

        getProxy().getPluginManager().registerCommand(this, new GravityCommand());

        getProxy().getPluginManager().registerCommand(this, new DiscordCommand());
        getProxy().getPluginManager().registerCommand(this, new HubCommand());
        getProxy().getPluginManager().registerCommand(this, new MapCommand());
        getProxy().getPluginManager().registerCommand(this, new PingCommand());
        getProxy().getPluginManager().registerCommand(this, new PrivacyCommand());
        getProxy().getPluginManager().registerCommand(this, new StoreCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportCommand());

        getProxy().getPluginManager().registerListener(this, new ProtocolChangeListener());
        getProxy().getPluginManager().registerListener(this, new ServerMoveListener());
    }

    public static Gravity getGravity() {
        return gravity;
    }
}
