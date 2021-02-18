package xyz.novaserver.gravity.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import xyz.novaserver.gravity.util.Config;

public class PrivacyCommand extends Command {
    public PrivacyCommand() {
        super("privacy");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent message = new TextComponent(Config.getColoredString("messages.privacy.message"));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Config.getString("messages.privacy.link")));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(Config.getColoredString("messages.privacy.hover")).create()));

        sender.sendMessage(message);
    }
}
