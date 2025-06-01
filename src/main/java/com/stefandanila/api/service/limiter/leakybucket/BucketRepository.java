package com.stefandanila.api.service.limiter.leakybucket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<BucketEntity, Long> {
    Optional<BucketEntity> findByClientIdAndPath(String clientId, String path);
}
