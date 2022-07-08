package xyz.novaserver.gravity.limbo;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.gravity.Gravity;
import xyz.novaserver.gravity.util.Config;

import java.time.Duration;

public class LimboFeature {
    private final Gravity gravity;
    private final LimboData limboData;

    private final String limboServerName;
    private RegisteredServer limboServer;
    private boolean limboOnline = false;

    public LimboFeature(Gravity gravity) {
        this.gravity = gravity;
        limboData = new LimboData();

        ConfigurationNode limboNode = Config.getRoot().getNode("resource-pack-limbo");

        limboServerName = limboNode.getNode("limbo-server").getString("limbo");
        loadLimboServer();
        gravity.getProxy().getScheduler()
                .buildTask(gravity, this::runLimboOnlineCheck)
                .delay(Duration.ofSeconds(1))
                .repeat(Duration.ofSeconds(limboNode.getNode("online-check").getLong(30)))
                .schedule();

        gravity.getProxy().getEventManager().register(gravity, new LimboListener(this));
    }

    public Gravity getGravity() {
        return gravity;
    }

    public LimboData getLimboData() {
        return limboData;
    }

    public RegisteredServer getLimboServer() {
        return limboServer;
    }

    public boolean isLimboOnline() {
        return limboOnline;
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        loadLimboServer();
    }

    private void runLimboOnlineCheck() {
        if (limboServer != null) {
            limboServer.ping().whenComplete((serverPing, throwable) -> {
                if (throwable != null && limboOnline) {
                    gravity.getLogger().warn("Limbo server went offline!", throwable);
                    limboOnline = false;
                } else if (throwable == null && !limboOnline) {
                    gravity.getLogger().info("Limbo server is back online!");
                    limboOnline = true;
                }
            });
        } else {
            limboOnline = false;
        }
    }

    private void loadLimboServer() {
        limboServer = gravity.getProxy().getServer(limboServerName).orElse(null);
    }
}
