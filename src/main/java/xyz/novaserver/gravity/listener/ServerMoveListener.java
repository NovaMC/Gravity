package xyz.novaserver.gravity.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.novaserver.gravity.util.Config;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;

public class ServerMoveListener implements Listener {

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        ServerInfo serverKickTo = ProxyServer.getInstance().getServerInfo(Config.getString("hub.server-name"));
        ServerInfo serverKickedFrom = event.getKickedFrom();

        if (!serverKickedFrom.equals(serverKickTo)) {
            String kickReason = BaseComponent.toLegacyText(event.getKickReasonComponent());
            Iterator<String> iterator = Config.getStringList("hub.blacklist").iterator();

            while (iterator.hasNext()) {
                String next = iterator.next();
                if (kickReason.toLowerCase().contains(next.toLowerCase())) {
                    return;
                }
            }

            if (!isOnline(serverKickTo.getSocketAddress())) {
                return;
            }

            event.setCancelled(true);
            event.setCancelServer(serverKickTo);
            if (!kickReason.isEmpty()) {
                event.getPlayer().sendMessage(event.getKickReasonComponent());
            }
        }
    }

    private boolean isOnline(SocketAddress address) {
        try {
            Socket socket = new Socket();
            socket.connect(address);
            socket.close();
            return true;
        }
        catch (IOException ignored) {
        }

        return false;
    }
}
