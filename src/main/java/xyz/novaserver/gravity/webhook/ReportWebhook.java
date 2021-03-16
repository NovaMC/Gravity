package xyz.novaserver.gravity.webhook;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.gravity.util.Config;

import java.time.Instant;

public class ReportWebhook {
    private final WebhookClient client;
    private final String avatarUrl;

    public ReportWebhook() {
        ConfigurationNode reportNode = Config.getRoot().getNode("report");
        this.client = WebhookClient.withUrl(reportNode.getNode("webhook-url").getString());
        this.avatarUrl = reportNode.getNode("avatar-url").getString();
    }

    public void sendReport(String player, String uuid, String reason, String reporter) {
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xE03434)
                .setTitle(new WebhookEmbed.EmbedTitle("Player Reported", null))
                .setAuthor(new WebhookEmbed.EmbedAuthor(player, String.format(avatarUrl, uuid), null))
                .setDescription("**Reason:** " + reason + "\n**Reporter:** " + reporter)
                .setTimestamp(Instant.now())
                .build();
        client.send(embed);
    }
}
