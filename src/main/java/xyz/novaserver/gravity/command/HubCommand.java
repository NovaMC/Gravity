package xyz.novaserver.gravity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.gravity.Gravity;
import xyz.novaserver.gravity.util.Config;

import java.util.Optional;

public class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        ConfigurationNode hubNode = Config.getRoot().getNode("hub");

        if (!(source instanceof Player)) {
            return;
        }

        String serverName = hubNode.getNode("server-name").getString();

        Player player = (Player) source;
        Optional<RegisteredServer> toConnect = Gravity.getInstance().getProxy().getServer(serverName);
        player.createConnectionRequest(toConnect.get()).fireAndForget();

        source.sendMessage(Component.text(hubNode.getNode("message").getString()));
    }
}
