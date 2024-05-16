//package com.learningspringboot.samah.employees.controller;
//
//import com.learningspringboot.samah.employees.service.impl.EmployeeServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest
//class EmployeeControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    EmployeeServiceImpl service;
//
//    @Test
//    void shouldReturnAppInfo() throws Exception {
//        this.mockMvc.perform(get("/info")).andDo(print()).andExpect(status().isOk()).andExpect(content().
//                   string(containsString("This application is Employees management system and this version: v1")));
//    }
//
//    @Test
//    void addEmployee() {
//    }
//
//    @Test
//    void editEmployee() {
//    }
//
//    @Test
//    void getEmployee() {
//    }
//
//    @Test
//    void deleteEmployee() {
//    }
//}
