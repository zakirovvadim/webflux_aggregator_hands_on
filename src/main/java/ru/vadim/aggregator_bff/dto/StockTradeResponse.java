package ru.vadim.aggregator_bff.dto;

import ru.vadim.aggregator_bff.domain.Ticker;
import ru.vadim.aggregator_bff.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance) {
}
