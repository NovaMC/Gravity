package xyz.novaserver.gravity.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.novaserver.gravity.util.Config;

public class HubCommand extends Command {
    public HubCommand() {
        super("hub", null, "lobby");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServerInfo(Config.getString("hub.server-name")));

            TextComponent message = new TextComponent(Config.getColoredString("hub.message"));
            sender.sendMessage(message);
        }
    }
}
