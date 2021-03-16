package xyz.novaserver.gravity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.gravity.util.Config;

public class MapCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        ConfigurationNode mapNode = Config.getRoot().getNode("map");

        TextComponent.Builder text = Component.text();
        text.content(mapNode.getNode("message").getString());
        text.clickEvent(ClickEvent.openUrl(mapNode.getNode("link").getString()));
        text.hoverEvent(HoverEvent.showText(Component.text(mapNode.getNode("hover").getString())));

        source.sendMessage(text.build());
    }
}
