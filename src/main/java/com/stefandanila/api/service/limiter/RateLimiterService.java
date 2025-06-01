package com.stefandanila.api.service.limiter;

import com.stefandanila.api.service.client.ClientService;
import com.stefandanila.api.service.limiter.leakybucket.BucketRepository;
import com.stefandanila.api.service.limiter.leakybucket.LeakyBucketRateLimiter;
import com.stefandanila.api.service.limiter.slidingwindow.SlidingWindowRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
public class RateLimiterService {

    private ClientService clientService;
    private BucketRepository bucketRepository;

    private final ConcurrentHashMap <String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

    @Autowired
    public RateLimiterService(ClientService clientService, BucketRepository bucketRepository) {
        this.clientService = clientService;
        this.bucketRepository = bucketRepository;
    }

    public boolean isRequestAllowed(String clientId, String path) {
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(path, strategySelector);
        return rateLimiter.isRequestAllowed(clientId);
    }

    private final Function<String, RateLimiter> strategySelector = path -> {
        if (path.equals("/foo")) {
            return new SlidingWindowRateLimiter(clientService, "/foo");
        }
        if (path.equals("/bar")) {
            return new LeakyBucketRateLimiter(bucketRepository, clientService,"/bar");
        }
        return new RateLimiter(clientService);
    };
}
