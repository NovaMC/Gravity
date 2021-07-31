package xyz.novaserver.gravity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.geysermc.floodgate.api.FloodgateApi;
import xyz.novaserver.gravity.Gravity;
import xyz.novaserver.gravity.util.Config;

import java.util.*;

public class BedrockJoinListener {
    private final FloodgateApi floodgate = FloodgateApi.getInstance();
    private final Set<UUID> firstJoin = new HashSet<>();

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        firstJoin.remove(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (floodgate.isFloodgatePlayer(player.getUniqueId())) {
            firstJoin.add(player.getUniqueId());
        }
    }

    @Subscribe
    public void onConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        String serverName = event.getOriginalServer().getServerInfo().getName();
        String firstName = Config.getRoot().getNode("bedrock").getNode("first-server").getString();
        String newName = Config.getRoot().getNode("bedrock").getNode("to-server").getString();

        if (firstJoin.contains(player.getUniqueId()) && serverName.equals(firstName)) {
            RegisteredServer newServer = Gravity.getInstance().getProxy().getServer(newName).orElseThrow();
            event.setResult(ServerPreConnectEvent.ServerResult.allowed(newServer));
            firstJoin.remove(player.getUniqueId());
        }
    }
}
