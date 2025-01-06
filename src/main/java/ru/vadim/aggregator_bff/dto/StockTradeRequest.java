package ru.vadim.aggregator_bff.dto;


import ru.vadim.aggregator_bff.domain.Ticker;
import ru.vadim.aggregator_bff.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction action) {
}
