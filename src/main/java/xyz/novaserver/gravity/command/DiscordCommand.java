package xyz.novaserver.gravity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.gravity.util.Config;

public class DiscordCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        ConfigurationNode discordNode = Config.getRoot().getNode("discord");

        TextComponent.Builder text = Component.text();
        text.content(discordNode.getNode("message").getString());
        text.clickEvent(ClickEvent.openUrl(discordNode.getNode("link").getString()));
        text.hoverEvent(HoverEvent.showText(Component.text(discordNode.getNode("hover").getString())));

        source.sendMessage(text.build());
    }
}
