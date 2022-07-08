package xyz.novaserver.gravity.limbo;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerResourcePackStatusEvent;
import com.velocitypowered.api.event.player.PlayerResourcePackStatusEvent.Status;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class LimboListener {
    private final LimboFeature limbo;
    private final FloodgateApi floodgate = FloodgateApi.getInstance();

    public LimboListener(LimboFeature limbo) {
        this.limbo = limbo;
    }

    @Subscribe
    public void onPlayerLogin(PostLoginEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        // Return if player is on Bedrock
        if (floodgate.isFloodgatePlayer(uuid)) {
            return;
        }

        // Store initial resource pack status
        final LimboData limboData = limbo.getLimboData();
        if (!limboData.hasStatus(uuid)) {
            limboData.createStatus(uuid).rpStatus(player.getAppliedResourcePack() != null);
        }
    }

    @Subscribe
    public void onPlayerConnect(ServerPreConnectEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        // Return if player is on Bedrock
        if (floodgate.isFloodgatePlayer(uuid)) {
            return;
        }

        final LimboData.Status status = limbo.getLimboData().getStatus(uuid);
        // Send to limbo if resource pack not applied
        if (limbo.isLimboOnline() && !status.rpStatus()) {
            // Store the current server for later use
            event.getResult().getServer().ifPresent(status::toConnect);

            // Send player to limbo
            final RegisteredServer limboServer = limbo.getLimboServer();
            event.setResult(ServerPreConnectEvent.ServerResult.allowed(limboServer));
        }
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        // Remove limbo data on disconnect
        limbo.getLimboData().removeStatus(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        final Player player = event.getPlayer();
        final LimboData limboData = limbo.getLimboData();

        // Should be impossible
        if (!limboData.hasStatus(player.getUniqueId())) {
            return;
        }

        LimboData.Status status = limboData.getStatus(player.getUniqueId());
        if (event.getStatus() == Status.SUCCESSFUL) {
            status.rpStatus(true);
            // Connect player to the server they would have connected to if they are currently in limbo
            player.getCurrentServer().ifPresent(connection -> {
                if (connection.getServer().equals(limbo.getLimboServer())) {
                    player.createConnectionRequest(status.toConnect()).fireAndForget();
                }
            });
        } else {
            status.rpStatus(false);
        }
    }
}
