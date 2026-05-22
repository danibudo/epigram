package com.dani.epigramapi.controller;

import com.dani.epigramapi.dto.EpigramDto;
import com.dani.epigramapi.dto.EpigramRequest;
import com.dani.epigramapi.exception.DuplicateEpigramException;
import com.dani.epigramapi.exception.GlobalExceptionHandler;
import com.dani.epigramapi.service.EpigramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EpigramControllerTest {

    @Mock
    private EpigramService service;

    @InjectMocks
    private EpigramController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private EpigramDto dto(String text) {
        return new EpigramDto(1, text, "Author", "Source", LocalDateTime.now());
    }

    @Test
    void getRandom_returns200WithEpigram() throws Exception {
        when(service.getRandom()).thenReturn(dto("A wise saying"));

        mockMvc.perform(get("/api/epigrams/random"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("A wise saying"));
    }

    @Test
    void getRandom_returns404_whenNoEpigramsExist() throws Exception {
        when(service.getRandom()).thenThrow(new NoSuchElementException("No epigrams found"));

        mockMvc.perform(get("/api/epigrams/random"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No epigrams found"));
    }

    @Test
    void getAll_returns200WithList() throws Exception {
        when(service.getAll()).thenReturn(List.of(dto("Quote 1"), dto("Quote 2")));

        mockMvc.perform(get("/api/epigrams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void create_returns201WithCreatedEpigram() throws Exception {
        EpigramRequest request = new EpigramRequest("New quote", "Author", "Source");
        when(service.create(any())).thenReturn(dto("New quote"));

        mockMvc.perform(post("/api/epigrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("New quote"));
    }

    @Test
    void create_returns400_whenTextIsBlank() throws Exception {
        EpigramRequest request = new EpigramRequest("", null, null);

        mockMvc.perform(post("/api/epigrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returns409_whenDuplicate() throws Exception {
        EpigramRequest request = new EpigramRequest("Duplicate", null, null);
        when(service.create(any())).thenThrow(new DuplicateEpigramException("An epigram with that text already exists"));

        mockMvc.perform(post("/api/epigrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("An epigram with that text already exists"));
    }
}