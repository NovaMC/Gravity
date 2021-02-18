package xyz.novaserver.gravity.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import xyz.novaserver.gravity.util.Config;

public class GravityCommand extends Command {
    public GravityCommand() {
        super("gravity");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent message = new TextComponent();

        if (!sender.hasPermission("gravity.admin")) {
            sender.sendMessage(new TextComponent(Config.getColoredString("no-permission")));
            return;
        }

        if (args.length < 1 || !args[0].equals("reload")) {
            message.setText(ChatColor.translateAlternateColorCodes('&',
                    "&cPlease use &a/gravity reload &cto reload the plugin."));
        }
        else {
            Config.loadConfig();
            message.setText(ChatColor.AQUA + "Reloaded the config for Gravity!");
        }

        sender.sendMessage(message);
    }
}
