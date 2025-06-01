package com.stefandanila.api.service.limiter.leakybucket;

import com.stefandanila.api.service.client.ClientService;
import com.stefandanila.api.service.client.Interval;
import com.stefandanila.api.service.client.RateLimit;
import com.stefandanila.api.service.limiter.RateLimiter;

import java.time.Duration;
import java.time.Instant;


public class LeakyBucketRateLimiter extends RateLimiter {
    private final BucketRepository repository;
    private final String path;

    public LeakyBucketRateLimiter(BucketRepository repository, ClientService clientService, String path) {
        super(clientService);
        this.repository = repository;
        this.path = path;
    }

    public synchronized boolean isRequestAllowed(String clientId) {
        Instant now = Instant.now().truncatedTo(getClientService().getRateLimitFor(clientId, path).getInterval().getUnit());

        BucketEntity bucket = repository.findByClientIdAndPath(clientId, path).orElseGet(() -> {
            BucketEntity newBucket = createBucket(clientId, path, now);
            return repository.save(newBucket);
        });

        long elapsedMillis = Duration.between(bucket.getLastUpdateTime(), now).toMillis();
        double leaked = (elapsedMillis / 1000.0) * bucket.getLeakRatePerSecond();

        bucket.setWaterLevel(Math.max(0.0, bucket.getWaterLevel() - leaked));
        bucket.setLastUpdateTime(now);

        if (bucket.getWaterLevel() < bucket.getCapacity()) {
            bucket.setWaterLevel(bucket.getWaterLevel() + 1.0);
            repository.save(bucket);
            return true;
        } else {
            repository.save(bucket);
            return false;
        }
    }

    private BucketEntity createBucket(String clientId, String path, Instant now) {
        BucketEntity bucket = new BucketEntity();
        bucket.setWaterLevel(0.0);
        bucket.setLastUpdateTime(now);
        bucket.setPath(path);
        bucket.setClientId(clientId);
        RateLimit rateLimit = getClientService().getRateLimitFor(clientId, path);
        bucket.setCapacity(rateLimit.getNumberOfRequests());
        Interval interval = rateLimit.getInterval();
        bucket.setLeakRatePerSecond(1.0 / Duration.of(interval.getValue(), interval.getUnit()).getSeconds());
        return bucket;
    }
}
