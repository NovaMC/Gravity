package xyz.novaserver.gravity.listener;

import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.novaserver.gravity.util.Config;

public class ProtocolChangeListener implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        event.getResponse().getVersion().setName(Config.getString("protocol"));
    }
}
