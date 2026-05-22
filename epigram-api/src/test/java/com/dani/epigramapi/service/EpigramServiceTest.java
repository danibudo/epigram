package com.dani.epigramapi.service;

import com.dani.epigramapi.dto.EpigramDto;
import com.dani.epigramapi.dto.EpigramRequest;
import com.dani.epigramapi.exception.DuplicateEpigramException;
import com.dani.epigramapi.model.Epigram;
import com.dani.epigramapi.repository.EpigramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EpigramServiceTest {

    @Mock
    private EpigramRepository repository;

    @InjectMocks
    private EpigramService service;

    private Epigram epigram(String text) {
        return Epigram.builder().id(1).text(text).author("Author").source("Source").build();
    }

    @Test
    void getRandom_returnsDto_whenEpigramExists() {
        when(repository.findRandom()).thenReturn(Optional.of(epigram("Test quote")));

        EpigramDto result = service.getRandom();

        assertThat(result.text()).isEqualTo("Test quote");
        assertThat(result.author()).isEqualTo("Author");
    }

    @Test
    void getRandom_throwsNoSuchElementException_whenRepositoryIsEmpty() {
        when(repository.findRandom()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRandom())
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No epigrams found");
    }

    @Test
    void create_stripsTextAndReturnsDto() {
        EpigramRequest request = new EpigramRequest("  New quote  ", "Author", "Source");
        when(repository.save(any(Epigram.class))).thenReturn(epigram("New quote"));

        EpigramDto result = service.create(request);

        assertThat(result.text()).isEqualTo("New quote");
    }

    @Test
    void create_throwsDuplicateEpigramException_onConstraintViolation() {
        EpigramRequest request = new EpigramRequest("Duplicate", null, null);
        when(repository.save(any(Epigram.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(DuplicateEpigramException.class)
                .hasMessage("An epigram with that text already exists");
    }

    @Test
    void getAll_returnsMappedList() {
        when(repository.findAll()).thenReturn(List.of(epigram("Quote 1"), epigram("Quote 2")));

        List<EpigramDto> result = service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(EpigramDto::text).containsExactly("Quote 1", "Quote 2");
    }
}