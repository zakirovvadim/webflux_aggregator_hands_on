package ru.vadim.aggregator_bff.dto;


import ru.vadim.aggregator_bff.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
