package com.stefandanila.api.limiter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class AuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testKnownClientId() throws Exception {
        mockMvc.perform(get("/foo").header("Authorization", "Bearer client-a")).andExpect(status().isOk());
        mockMvc.perform(get("/bar").header("Authorization", "Bearer client-a")).andExpect(status().isOk());
        mockMvc.perform(get("/foo").header("Authorization", "Bearer client-b")).andExpect(status().isOk());
        mockMvc.perform(get("/bar").header("Authorization", "Bearer client-b")).andExpect(status().isOk());
    }

    @Test
    void testUnknownClientId() throws Exception {
        mockMvc.perform(get("/foo").header("Authorization", "Bearer unknown")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/bar").header("Authorization", "Bearer unknown")).andExpect(status().isUnauthorized());
    }

    @Test
    void testUnknownPath() throws Exception {
        mockMvc.perform(get("/abc").header("Authorization", "Bearer client-a")).andExpect(status().isNotFound());
    }
}

