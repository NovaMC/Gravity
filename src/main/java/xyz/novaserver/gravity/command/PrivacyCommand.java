package xyz.novaserver.gravity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.gravity.util.Config;

public class PrivacyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        ConfigurationNode privacyNode = Config.getRoot().getNode("privacy");

        TextComponent.Builder text = Component.text();
        text.content(privacyNode.getNode("message").getString());
        text.clickEvent(ClickEvent.openUrl(privacyNode.getNode("link").getString()));
        text.hoverEvent(HoverEvent.showText(Component.text(privacyNode.getNode("hover").getString())));

        source.sendMessage(text.build());
    }
}
