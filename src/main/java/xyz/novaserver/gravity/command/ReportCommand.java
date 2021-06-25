package xyz.novaserver.gravity.command;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.Types;
import xyz.novaserver.gravity.Gravity;
import xyz.novaserver.gravity.util.Config;
import xyz.novaserver.gravity.webhook.ReportWebhook;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportCommand implements SimpleCommand {
    private ReportWebhook webhook = null;
    private final ConfigurationNode reportNode;

    public ReportCommand() {
        this.reportNode = Config.getRoot().getNode("report");

        if (reportNode.getNode("webhook-enabled").getBoolean()) {
            webhook = new ReportWebhook();
        }
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        TextComponent.Builder text = Component.text();

        if (!(source instanceof Player)) {
            return;
        }

        if (args.length == 0) {
            text.content("Usage: /report <player> <reason>").color(NamedTextColor.RED);
        }
        else if (args.length == 1) {
            text.content("Please provide a reason with your report!").color(NamedTextColor.RED);
        }
        else if (Gravity.getInstance().getProxy().getPlayer(args[0]).isEmpty()) {
            text.content("That player isn't online.").color(NamedTextColor.RED);
        }
        else {
            // Join arguments to put the reason into a string
            String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            // Set text for reply message
            text.content(String.format(reportNode.getNode("message").getString(), args[0]));

            // Send the report to the webhook
            if (webhook != null) {
                webhook.sendReport(args[0],
                        Gravity.getInstance().getProxy().getPlayer(args[0]).get().getUniqueId().toString(), reason,
                        ((Player) source).getUsername());
            }

            // Create the admin report message
            TextComponent.Builder adminText = Component.text();
            adminText.content(String.join("\n", reportNode.getNode("admin-message").getList(Types::asString))
                    .replaceAll("%name%", args[0]).replaceAll("%reason%", reason)
                    .replaceAll("%reporter%", ((Player) source).getUsername()));

            // Send report to all players with notify permission
            Gravity.getInstance().getProxy().getAllPlayers().stream()
                    .filter(player -> player.hasPermission("gravity.reports.notify"))
                    .forEach(player -> player.sendMessage(adminText));
        }

        source.sendMessage(text);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        Stream<String> possibilities = Gravity.getInstance().getProxy().getAllPlayers()
                .stream().map(Player::getUsername);

        if (args.length == 0) {
            return possibilities.collect(Collectors.toList());
        }
        else if (args.length == 1) {
            return possibilities
                    .filter(name -> name.regionMatches(true, 0, args[0], 0, args[0].length()))
                    .collect(Collectors.toList());
        }
        else {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("gravity.command.report");
    }
}
