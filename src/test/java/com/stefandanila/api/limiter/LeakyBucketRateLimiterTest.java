package com.stefandanila.api.limiter;

import com.stefandanila.api.service.client.ClientService;
import com.stefandanila.api.service.limiter.leakybucket.BucketRepository;
import com.stefandanila.api.service.limiter.leakybucket.LeakyBucketRateLimiter;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LeakyBucketRateLimiterTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientService clientService;
    @Autowired
    private BucketRepository repository;

    @Test
    void testLeakyBucketBehavior() throws InterruptedException {
        LeakyBucketRateLimiter limiter = new LeakyBucketRateLimiter(repository, clientService, "/foo");

        String client = "client-b";

        assertTrue(limiter.isRequestAllowed(client));
        assertTrue(limiter.isRequestAllowed(client));
        assertTrue(limiter.isRequestAllowed(client));


        assertFalse(limiter.isRequestAllowed(client));

        Thread.sleep(1100);

        assertTrue(limiter.isRequestAllowed(client));
        assertFalse(limiter.isRequestAllowed(client));
    }
}

