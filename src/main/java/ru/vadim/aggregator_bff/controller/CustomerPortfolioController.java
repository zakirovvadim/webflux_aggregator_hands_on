package ru.vadim.aggregator_bff.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.vadim.aggregator_bff.dto.CustomerInformation;
import ru.vadim.aggregator_bff.dto.StockTradeRequest;
import ru.vadim.aggregator_bff.dto.StockTradeResponse;
import ru.vadim.aggregator_bff.dto.TradeRequest;
import ru.vadim.aggregator_bff.service.CustomerPortfolioService;
import ru.vadim.aggregator_bff.validator.RequestValidator;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> mono) {
        return mono.transform(RequestValidator.validate())
                .flatMap(req -> this.customerPortfolioService.trade(customerId, req));
    }
}
