package com.stefandanila.api.service.limiter.slidingwindow;

import com.stefandanila.api.service.client.ClientService;
import com.stefandanila.api.service.client.RateLimit;
import com.stefandanila.api.service.limiter.RateLimiter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindowRateLimiter extends RateLimiter {
    private final Map<String, Deque<Instant>> requestTimestamps = new ConcurrentHashMap<>();
    private final String path;

    public SlidingWindowRateLimiter(ClientService clientService, String path) {
        super(clientService);
        this.path = path;
    }

    @Override
    public boolean isRequestAllowed(String clientId) {
        RateLimit rateLimit = getClientService().getRateLimitFor(clientId ,path);
        int maxRequests = rateLimit.getNumberOfRequests();
        ChronoUnit chronoUnit = rateLimit.getInterval().getUnit();
        int value = rateLimit.getInterval().getValue();
        Duration interval = Duration.of(value, chronoUnit);
        Deque<Instant> timestamps = requestTimestamps.computeIfAbsent(clientId, k -> new ConcurrentLinkedDeque<>());
        Instant now = Instant.now().truncatedTo(chronoUnit);

        cleanOldTimestamps(timestamps, now, interval);

        boolean limitReached = timestamps.size() >= maxRequests;

        if (!limitReached) {
            timestamps.addLast(now);
        }

        return !limitReached;
    }

    private void cleanOldTimestamps(Deque<Instant> timestamps, Instant now, Duration interval) {
        Instant cutoff = now.minus(interval);
        while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(cutoff)) {
            timestamps.pollFirst();
        }
    }
}
