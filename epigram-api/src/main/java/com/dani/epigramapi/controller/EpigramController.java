package com.dani.epigramapi.controller;

import com.dani.epigramapi.dto.EpigramDto;
import com.dani.epigramapi.dto.EpigramRequest;
import com.dani.epigramapi.service.EpigramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epigrams")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EpigramController {

    private final EpigramService service;

    @GetMapping("/random")
    public ResponseEntity<EpigramDto> getRandom() {
        return ResponseEntity.ok(service.getRandom());
    }

    @GetMapping
    public ResponseEntity<List<EpigramDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<EpigramDto> create(@RequestBody EpigramRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}