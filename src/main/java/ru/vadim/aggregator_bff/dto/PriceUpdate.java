package ru.vadim.aggregator_bff.dto;

import ru.vadim.aggregator_bff.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker,
                          Integer price,
                          LocalDateTime time) {
}
