package com.dani.epigramapi.dto;

import com.dani.epigramapi.model.Epigram;
import java.time.LocalDateTime;

public record EpigramDto(
        Integer id,
        String text,
        String author,
        String source,
        LocalDateTime createdAt
) {
    public static EpigramDto from(Epigram epigram) {
        return new EpigramDto(
                epigram.getId(),
                epigram.getText(),
                epigram.getAuthor(),
                epigram.getSource(),
                epigram.getCreatedAt()
        );
    }
}