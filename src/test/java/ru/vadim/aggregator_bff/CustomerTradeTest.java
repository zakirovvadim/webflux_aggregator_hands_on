package ru.vadim.aggregator_bff;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.RegexBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.vadim.aggregator_bff.domain.Ticker;
import ru.vadim.aggregator_bff.domain.TradeAction;
import ru.vadim.aggregator_bff.dto.TradeRequest;

import java.util.Objects;

public class CustomerTradeTest extends AbstractIntegrationTest {

    public static final Logger log = LoggerFactory.getLogger(StockPriceStreamTest.class);

    @Test
    public void tradeSuccess() {
        mockCustomerTrade("customer-service/customer-trade-200.json", 200);
        var tradeRequest = new TradeRequest(Ticker.YANDEX, TradeAction.BUY, 2);
        postTrade(tradeRequest, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9780)
                .jsonPath("$.totalPrice").isEqualTo(220);
    }

    @Test
    public void tradeFailure() {
        mockCustomerTrade("customer-service/customer-trade-400.json", 400);
        var tradeRequest = new TradeRequest(Ticker.YANDEX, TradeAction.BUY, 2);
        postTrade(tradeRequest, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer [id=1] does not have enough funds to complete the transaction");
    }

    @Test
    public void inputValidation() {
        var missingTicker = new TradeRequest(null, TradeAction.BUY, 2);
        postTrade(missingTicker, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Ticker is required");

        var missingAction = new TradeRequest(Ticker.YANDEX, null, 2);
        postTrade(missingAction, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Trade action is required");

        var invalidQuantity = new TradeRequest(Ticker.YANDEX, TradeAction.BUY,-2);
        postTrade(invalidQuantity, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Quantity should be > 0");
    }

    private void mockCustomerTrade(String path, int responseCode) {
        // mock stock-service price resposne
        var stockResponseBody = this.resourceToString("stock-service/stock-price-200.json");
        mockServerClient
                .when(HttpRequest.request("/stock/YANDEX"))
                .respond(
                        HttpResponse.response(stockResponseBody)
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                );

        // првоеряем что отправялется нужный запрос через реджекс - интересует что отправялется именно цена
        // mock customer-service trade response
        var customerResponseBody = this.resourceToString(path);
        mockServerClient
                .when(
                        HttpRequest.request("/customers/1/trade")
                                .withMethod("POST")
                                .withBody(RegexBody.regex(".*\"price\":110.*")) // Tаким образом, мы подтверждаем, что наш агрегатор использует цену 110.
                )
                .respond(
                        HttpResponse.response(customerResponseBody)
                                .withStatusCode(responseCode)
                                .withContentType(MediaType.APPLICATION_JSON)
                );

    }

    private WebTestClient.BodyContentSpec postTrade(TradeRequest tradeRequest, HttpStatus expectStatus) {
        return this.client.post()
                .uri("/customers/1/trade")
                .bodyValue(tradeRequest)
                .exchange()
                .expectStatus().isEqualTo(expectStatus)
                .expectBody()
                .consumeWith(e -> log.info("{}", new String(Objects.requireNonNull(e.getResponseBody()))));
    }
}
