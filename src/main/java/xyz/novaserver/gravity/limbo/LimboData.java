package xyz.novaserver.gravity.limbo;

import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LimboData {
    private final Map<UUID, Status> limboStatuses = new HashMap<>();

    public Status getStatus(UUID uuid) {
        return limboStatuses.get(uuid);
    }

    public boolean hasStatus(UUID uuid) {
        return limboStatuses.containsKey(uuid);
    }

    public Status createStatus(UUID uuid) {
        limboStatuses.put(uuid, new Status());
        return limboStatuses.get(uuid);
    }

    public void removeStatus(UUID uuid) {
        limboStatuses.remove(uuid);
    }

    public static class Status {
        private boolean rpStatus;
        private RegisteredServer toConnect;

        private Status() {}

        public boolean rpStatus() {
            return rpStatus;
        }

        public Status rpStatus(boolean rpStatus) {
            this.rpStatus = rpStatus;
            return this;
        }

        public RegisteredServer toConnect() {
            return toConnect;
        }

        public Status toConnect(RegisteredServer toConnect) {
            this.toConnect = toConnect;
            return this;
        }
    }
}
