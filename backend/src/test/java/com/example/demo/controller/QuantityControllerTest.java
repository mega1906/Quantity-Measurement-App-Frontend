//package com.example.demo.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.MediaType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.example.demo.model.Unit;
//import com.example.demo.service.IQuantityMeasurementService;
//
//@WebMvcTest(QuantityControllerTest.class)
//class QuantityControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private IQuantityMeasurementService service;
//
//    @Test
//    void convert_api_should_return_ok() throws Exception {
//
//        when(service.convert(any(), eq(Unit.CENTIMETER)))
//                .thenReturn(100.0);
//
//        mockMvc.perform(
//                post("/api/v1/quantities/convert")
//                .param("targetUnit", "CENTIMETER")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("""
//                    { "value": 1, "unit": "METER" }
//                """))
//            .andExpect(status().isOk())
//            .andExpect(content().string("100.0"));
//    }
//}
