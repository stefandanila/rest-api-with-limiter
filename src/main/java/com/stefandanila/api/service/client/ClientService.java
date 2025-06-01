package com.stefandanila.api.service.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@ConfigurationProperties(prefix = "")
public class ClientService {

    private Set<ClientConfig> clients;

    public void setClients(Set<ClientConfig> clients) {
        this.clients = clients;
    }

    public ClientConfig getClient(String clientId) {
        for (ClientConfig clientConfig : clients) {
            if (clientConfig.getId().equals(clientId)) {
                return clientConfig;
            }
        }
        return null;
    }

    public RateLimit getRateLimitFor(String clientId, String path) {
        return clients.stream()
                .filter(client -> client.getId().equalsIgnoreCase(clientId))
                .flatMap(client -> client.getRateLimits().stream())
                .filter(limit -> limit.getPath().equalsIgnoreCase(path))
                .findFirst().orElse(new RateLimit());
    }

    public boolean isValid(String clientId) {
        return getClient(clientId) != null;
    }
}
