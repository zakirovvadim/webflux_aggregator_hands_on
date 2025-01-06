package ru.vadim.aggregator_bff.dto;

import ru.vadim.aggregator_bff.domain.Ticker;
import ru.vadim.aggregator_bff.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           TradeAction action,
                           Integer quantity){}
