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

import java.util.Optional;
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
        if (!limbo.hasData(uuid)) {
            limbo.createData(uuid).rpStatus(player.getAppliedResourcePack() != null);
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

        LimboFeature.LimboData data = limbo.getData(uuid);
        // Send to limbo if resource pack not applied
        if (!data.rpStatus()) {
            // Store the current server for later use
            event.getResult().getServer().ifPresent(data::toConnect);

            // Send player to limbo
            final Optional<RegisteredServer> limboServer = limbo.getGravity()
                    .getProxy().getServer(limbo.getLimboServer());
            limboServer.ifPresent(server -> event.setResult(ServerPreConnectEvent.ServerResult.allowed(server)));
        }
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        // Remove limbo data on disconnect
        limbo.removeData(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        final Status status = event.getStatus();
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        // Should be impossible
        if (!limbo.hasData(player.getUniqueId())) {
            return;
        }

        LimboFeature.LimboData data = limbo.getData(uuid);
        if (status == Status.SUCCESSFUL) {
            data.rpStatus(true);
            player.createConnectionRequest(data.toConnect()).fireAndForget();
        } else {
            data.rpStatus(false);
        }
    }
}
