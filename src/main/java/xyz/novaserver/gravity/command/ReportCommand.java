package xyz.novaserver.gravity.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.novaserver.gravity.util.Config;
import xyz.novaserver.gravity.webhook.ReportWebhook;

import java.util.Arrays;

public class ReportCommand extends Command {
    private final ReportWebhook webhook;

    public ReportCommand() {
        super("report");

        if (Config.getBoolean("report.webhook-enabled")) {
            webhook = new ReportWebhook();
        }
        else {
            webhook = null;
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent reply = new TextComponent();

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder(ChatColor.RED + "This command can only be used by players.").create());
            return;
        }

        if (!sender.hasPermission("gravity.reports.report")) {
            sender.sendMessage(new TextComponent(Config.getColoredString("no-permission")));
            return;
        }

        if (args.length < 1) {
            reply.setText(ChatColor.RED + "Usage: /report <player> <reason>");
        }
        else if (args.length < 2) {
            reply.setText(ChatColor.RED + "Please provide a reason with your report!");
        }
        else if (ProxyServer.getInstance().getPlayer(args[0]) == null) {
            reply.setText(Config.getColoredString("report.not-online"));
        }
        else {
            String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            TextComponent message = new TextComponent(String.join("\n",
                    Config.getColoredStringList("report.admin-message")).replaceAll("%name%", args[0])
                    .replaceAll("%reporter%", sender.getName()).replaceAll("%reason%", reason));

            if (webhook != null) {
                webhook.sendReport(ProxyServer.getInstance().getPlayer(args[0]), reason, sender.getName());
            }
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.hasPermission("gravity.reports.notify")) {
                    player.sendMessage(message);
                }
            }

            reply.setText(String.format(Config.getColoredString("report.message"), args[0]));
        }

        sender.sendMessage(reply);
    }
}
