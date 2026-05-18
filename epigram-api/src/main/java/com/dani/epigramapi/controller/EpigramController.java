package com.dani.epigramapi.controller;

import com.dani.epigramapi.dto.EpigramDto;
import com.dani.epigramapi.dto.EpigramRequest;
import com.dani.epigramapi.service.EpigramService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleConflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}