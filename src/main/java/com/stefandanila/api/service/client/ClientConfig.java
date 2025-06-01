package com.stefandanila.api.service.client;


import java.util.List;

public class ClientConfig {
    private String id;
    private List<RateLimit> rateLimits;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RateLimit> getRateLimits() {
        return rateLimits;
    }

    public void setRateLimits(List<RateLimit> rateLimits) {
        this.rateLimits = rateLimits;
    }
}

