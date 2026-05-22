package com.dani.epigramapi.service;

import com.dani.epigramapi.dto.EpigramDto;
import com.dani.epigramapi.dto.EpigramRequest;
import com.dani.epigramapi.exception.DuplicateEpigramException;
import com.dani.epigramapi.model.Epigram;
import com.dani.epigramapi.repository.EpigramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EpigramService {

    private final EpigramRepository repository;

    public EpigramDto getRandom() {
        return repository.findRandom()
                .map(EpigramDto::from)
                .orElseThrow(() -> new NoSuchElementException("No epigrams found"));
    }

    public EpigramDto create(EpigramRequest request) {
        if (request.text() == null || request.text().isBlank()) {
            throw new IllegalArgumentException("Text must not be blank");
        }
        try {
            Epigram epigram = Epigram.builder()
                    .text(request.text().strip())
                    .author(request.author())
                    .source(request.source())
                    .build();
            return EpigramDto.from(repository.save(epigram));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEpigramException("An epigram with that text already exists");
        }
    }

    public List<EpigramDto> getAll() {
        return repository.findAll().stream()
                .map(EpigramDto::from)
                .toList();
    }
}