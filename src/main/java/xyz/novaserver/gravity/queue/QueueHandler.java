package xyz.novaserver.gravity.queue;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import xyz.novaserver.gravity.Gravity;
import xyz.novaserver.gravity.util.Config;

import java.util.concurrent.TimeUnit;

public class QueueHandler {
    private final Gravity plugin;

    private final String fallbackServer;
    private final String newServer;

    private boolean serverOnline = false;
    private boolean backOnline = false;

    public QueueHandler(Gravity plugin) {
        this.plugin = plugin;
        this.fallbackServer = Config.getRoot().getNode("rejoin").getNode("fallback").getString();
        this.newServer = Config.getRoot().getNode("rejoin").getNode("new-server").getString();
        plugin.getProxy().getScheduler().buildTask(plugin, new StatusChecker()).delay(2, TimeUnit.SECONDS).repeat(2, TimeUnit.SECONDS).schedule();
    }

    public void startQueue() {
        if (backOnline) {
            // Change initial delay
            int delay = 5;
            for (Player player : plugin.getProxy().getServer(fallbackServer).orElseThrow().getPlayersConnected()) {
                String message = Config.getRoot().getNode("rejoin").getNode("message").getString().replace("%s", Integer.toString(delay));
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));

                plugin.getProxy().getScheduler().buildTask(plugin, () -> {
                    if (player.isActive()) {
                        player.createConnectionRequest(plugin.getProxy().getServer(newServer).orElseThrow()).fireAndForget();
                    }
                }).delay(delay, TimeUnit.SECONDS).schedule();
                delay++;
            }
            backOnline = false;
        }
    }

    private class StatusChecker implements Runnable {
        @Override
        public void run() {
            plugin.getProxy().getServer(newServer).orElseThrow().ping().handle((result, ex) -> {
                if (result == null) {
                    serverOnline = false;
                    backOnline = false;
                } else {
                    if (!serverOnline && !backOnline) {
                        backOnline = true;
                        startQueue();
                    }
                    serverOnline = true;
                }
                return null;
            });
        }
    }
}
