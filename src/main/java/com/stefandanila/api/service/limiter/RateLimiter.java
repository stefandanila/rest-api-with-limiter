package com.stefandanila.api.service.limiter;

import com.stefandanila.api.service.client.ClientService;

public class RateLimiter {
    private final ClientService clientService;

    public RateLimiter(ClientService clientService) {
        this.clientService = clientService;
    }

    public boolean isRequestAllowed(String clientId){
        return false;
    }

    public ClientService getClientService() {
        return clientService;
    }
}
