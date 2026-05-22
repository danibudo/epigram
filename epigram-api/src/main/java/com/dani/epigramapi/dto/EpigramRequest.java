package com.dani.epigramapi.dto;

import jakarta.validation.constraints.NotBlank;

public record EpigramRequest(
        @NotBlank(message = "Text must not be blank") String text,
        String author,
        String source
) {}