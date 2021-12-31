package xyz.novaserver.gravity;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public class GravityPluginMessageHandler {
    private final Gravity plugin;
    private final MinecraftChannelIdentifier CHANNEL_ID;

    public GravityPluginMessageHandler(Gravity plugin) {
        this.plugin = plugin;
        CHANNEL_ID = MinecraftChannelIdentifier.create("mechanics", "command");
        plugin.getProxy().getChannelRegistrar().register(CHANNEL_ID);
    }

    @Subscribe
    public void on(PluginMessageEvent event) {
        if (event.getIdentifier() != CHANNEL_ID) return;

        ByteArrayDataInput in = event.dataAsDataStream();
        String subchannel = in.readUTF();

        if (subchannel.equalsIgnoreCase("Console")) {
            String cmd = in.readUTF();
            plugin.getProxy().getCommandManager().executeAsync(plugin.getProxy().getConsoleCommandSource(), cmd);
        }
    }
}
