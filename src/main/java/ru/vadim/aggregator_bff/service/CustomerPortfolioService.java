package ru.vadim.aggregator_bff.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.aggregator_bff.client.CustomerServiceClient;
import ru.vadim.aggregator_bff.client.StockServiceClient;
import ru.vadim.aggregator_bff.dto.*;

@Service
public class CustomerPortfolioService {

    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public CustomerPortfolioService(StockServiceClient stockServiceClient, CustomerServiceClient customerServiceClient) {
        this.stockServiceClient = stockServiceClient;
        this.customerServiceClient = customerServiceClient;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> trade(Integer custometId, TradeRequest request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(price -> this.toStockTradeRequest(request, price))
                .flatMap(req -> this.customerServiceClient.trade(custometId, req));
    }

    private StockTradeRequest toStockTradeRequest(TradeRequest request, Integer price) {
        return new StockTradeRequest(
                request.ticker(),
                price,
                request.quantity(),
                request.action()
        );
    }
}
