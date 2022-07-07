package xyz.novaserver.gravity.limbo;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import xyz.novaserver.gravity.Gravity;
import xyz.novaserver.gravity.util.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LimboFeature {
    private final Gravity gravity;
    private final String limboServer;

    private final Map<UUID, LimboData> limboData = new HashMap<>();

    public LimboFeature(Gravity gravity) {
        this.gravity = gravity;
        limboServer = Config.getRoot().getNode("resource-pack-limbo", "limbo-server").getString();
        gravity.getProxy().getEventManager().register(gravity, new LimboListener(this));
    }

    public Gravity getGravity() {
        return gravity;
    }

    public String getLimboServer() {
        return limboServer;
    }

    public LimboData getData(UUID uuid) {
        return limboData.get(uuid);
    }

    public boolean hasData(UUID uuid) {
        return limboData.containsKey(uuid);
    }

    public LimboData createData(UUID uuid) {
        limboData.put(uuid, new LimboData());
        return limboData.get(uuid);
    }

    public void removeData(UUID uuid) {
        limboData.remove(uuid);
    }

    public static class LimboData {
        private boolean rpStatus;
        private RegisteredServer toConnect;

        private LimboData() {}

        public boolean rpStatus() {
            return rpStatus;
        }

        public LimboData rpStatus(boolean rpStatus) {
            this.rpStatus = rpStatus;
            return this;
        }

        public RegisteredServer toConnect() {
            return toConnect;
        }

        public LimboData toConnect(RegisteredServer toConnect) {
            this.toConnect = toConnect;
            return this;
        }
    }
}
