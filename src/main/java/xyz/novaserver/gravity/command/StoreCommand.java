package xyz.novaserver.gravity.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import xyz.novaserver.gravity.util.Config;

public class StoreCommand extends Command {
    public StoreCommand() {
        super("store", null, "donate");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent message = new TextComponent(Config.getColoredString("messages.store.message"));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Config.getString("messages.store.link")));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(Config.getColoredString("messages.store.hover")).create()));

        sender.sendMessage(message);
    }
}
