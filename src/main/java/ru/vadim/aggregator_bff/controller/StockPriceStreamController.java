package ru.vadim.aggregator_bff.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.vadim.aggregator_bff.client.StockServiceClient;
import ru.vadim.aggregator_bff.dto.PriceUpdate;

@RestController
@RequestMapping("stock")
public class StockPriceStreamController {

    private final StockServiceClient stockServiceClient; // лучше не использовать на прямую в контроллере, а нужн очерез сервис, но тут логика простая и для примера

    public StockPriceStreamController(StockServiceClient stockServiceClient) {
        this.stockServiceClient = stockServiceClient;
    }

    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceUpdate> priceUpdatesStream() {
        return this.stockServiceClient.priceUpdatesStream();
    }
}
