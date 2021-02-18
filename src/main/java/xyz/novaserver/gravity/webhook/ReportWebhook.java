package xyz.novaserver.gravity.webhook;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.novaserver.gravity.util.Config;

import java.time.Instant;

public class ReportWebhook {
    private final WebhookClient client;
    private final String avatarUrl;

    public ReportWebhook() {
        this.client = WebhookClient.withUrl(Config.getString("report.webhook-url"));
        avatarUrl = "https://crafatar.com/avatars/%s.png";
    }

    public void sendReport(ProxiedPlayer player, String reason, String reporter) {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xE03434)
                .setTitle(new WebhookEmbed.EmbedTitle("Player Reported", null))
                .setAuthor(new WebhookEmbed.EmbedAuthor(player.getName(), String.format(avatarUrl, player.getUniqueId().toString()), null))
                .setDescription("**Reason:** " + reason + "\n**Reporter:** " + reporter)
                .setTimestamp(Instant.now())
                .build();
        client.send(embed);
    }
}
