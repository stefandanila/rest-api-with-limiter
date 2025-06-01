package com.stefandanila.api.limiter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class SlidingWindowRateLimiterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSlidingWindowRateLimitOverTime() throws Exception {
        int count = 0;
        for (int i = 0; i < 100; i++) {
            if (repeatedTest()) {
                count++;
                Thread.sleep(100);
            }
        }
        assertEquals(100, count);
    }

    private boolean repeatedTest() throws Exception {
        String clientId = "client-a";
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/foo").header("Authorization", "Bearer " + clientId));
        }

        int status = mockMvc.perform(get("/foo").header("Authorization", "Bearer " + clientId)).andReturn().getResponse().getStatus();
        return status == 429;
    }
}

