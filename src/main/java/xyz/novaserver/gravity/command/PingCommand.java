package xyz.novaserver.gravity.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.novaserver.gravity.util.Config;

public class PingCommand extends Command {
    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent message = new TextComponent();

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder(ChatColor.RED + "This command can only be used by players.").create());
            return;
        }

        if (args.length < 1) {
            message.setText(String.format(Config.getColoredString("messages.ping.your-ping"),
                    ((ProxiedPlayer) sender).getPing()));
        }
        else {
            ProxiedPlayer otherPlayer = ProxyServer.getInstance().getPlayer(args[0]);
            if (otherPlayer != null) {
                message.setText(String.format(Config.getColoredString("messages.ping.others-ping"),
                        otherPlayer.getName(), otherPlayer.getPing()));
            }
            else {
                message.setText(Config.getColoredString("messages.ping.not-online"));
            }
        }

        sender.sendMessage(message);
    }
}
