package com.stefandanila.api.service.limiter.leakybucket;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "bucket", uniqueConstraints = {@UniqueConstraint(columnNames = {"client_id", "path"})})
public class BucketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "water_level", nullable = false)
    private double waterLevel;

    @Column(name = "last_update_time", nullable = false)
    private Instant lastUpdateTime;

    @Column(name = "capacity", nullable = false)
    private long capacity;

    @Column(name = "leak_rate_per_second", nullable = false)
    private double leakRatePerSecond;

    public BucketEntity() {
    }

    public BucketEntity(String clientId, String path, double waterLevel, Instant lastUpdateTime, long capacity, double leakRatePerSecond) {
        this.clientId = clientId;
        this.path = path;
        this.waterLevel = waterLevel;
        this.lastUpdateTime = lastUpdateTime;
        this.capacity = capacity;
        this.leakRatePerSecond = leakRatePerSecond;
    }

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    public Instant getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Instant lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public double getLeakRatePerSecond() {
        return leakRatePerSecond;
    }

    public void setLeakRatePerSecond(double leakRatePerSecond) {
        this.leakRatePerSecond = leakRatePerSecond;
    }
}
