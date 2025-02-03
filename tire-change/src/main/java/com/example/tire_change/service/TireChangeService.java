package com.example.tire_change.service;
import com.example.tire_change.config.TireShopConfig;
import com.example.tire_change.dto.TireChangeTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class TireChangeService {
    private final WebClient webClient;
    private final TireShopConfig tireShopConfig;

    @Autowired
    public TireChangeService(WebClient webClient, TireShopConfig tireShopConfig) {
        this.webClient = webClient;
        this.tireShopConfig = tireShopConfig;
    }

    public Mono<List<TireChangeTime>> getAvailableTimes(String city, String from, String until) {
        TireShopConfig.TireShop shop = getShopByCity(city);

        MediaType mediaType = getMediaType(shop);

        return webClient.get()
                .uri(shop.getBaseUrl() + shop.getTimeEndpoint() + "?from=" + from + "&until=" + until)
                .accept(mediaType)
                .retrieve()
                .bodyToMono(TireChangeTime.XmlResponse.class)
                .map(response -> response.toList());
    }


    public Mono<String> bookTireChange(String city, String id, String contactInfo) {
        TireShopConfig.TireShop shop = getShopByCity(city);

        MediaType mediaType = getMediaType(shop);
        HttpMethod httpMethod = shop.getFormat().equalsIgnoreCase("xml") ? HttpMethod.PUT : HttpMethod.POST;

        String bookingUrl = shop.getBaseUrl() + shop.getBookingEndpoint().replace("{id}", id).replace("{uuid}", id);

        return webClient.method(httpMethod)
                .uri(bookingUrl)
                .contentType(mediaType)
                .bodyValue(new TireChangeTime.BookingRequest(contactInfo))
                .retrieve()
                .bodyToMono(String.class);
    }


    private TireShopConfig.TireShop getShopByCity(String city) {

        return tireShopConfig.getShops().stream()
                .filter(s -> s.getName().equalsIgnoreCase(city))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown city"));
    }

    private MediaType getMediaType(TireShopConfig.TireShop shop) {
        return shop.getFormat().equalsIgnoreCase("xml") ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;
    }
}