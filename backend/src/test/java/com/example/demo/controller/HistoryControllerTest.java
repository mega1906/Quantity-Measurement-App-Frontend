package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.repository.QuantityMeasurementRepository;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuantityMeasurementRepository repository;

    @Test
    void get_history_should_return_ok() throws Exception {
        mockMvc.perform(get("/history"))
            .andExpect(status().isOk());
    }

    @Test
    void delete_history_should_clear_db() throws Exception {
        repository.deleteAll();

        mockMvc.perform(delete("/history"))
            .andExpect(status().isOk());
    }
}
