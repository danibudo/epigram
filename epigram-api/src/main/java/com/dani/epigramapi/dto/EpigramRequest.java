package com.dani.epigramapi.dto;

public record EpigramRequest(
        String text,
        String author,
        String source
) {}