package ru.vadim.aggregator_bff.dto;

import ru.vadim.aggregator_bff.domain.Ticker;

public record StockPriceResponse(Ticker ticker,
                                 Integer price) {
}
