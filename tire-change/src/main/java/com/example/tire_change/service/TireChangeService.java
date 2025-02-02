package service;


import config.TireShopConfig;
import dto.TireChangeTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TireChangeService {
    private final WebClient webClient;
    private final TireShopConfig tireShopConfig;

    // Доступные времена
    public Mono<List<TireChangeTime>> getAvailableTimes(String city, String from, String until) {
        TireShopConfig.TireShop shop = getShopByCity(city);

        MediaType mediaType = getMediaType(shop);

        return webClient.get()
                .uri(shop.getBaseUrl() + shop.getTimeEndpoint() + "?from=" + from + "&until=" + until)
                .accept(mediaType)
                .exchangeToMono(response -> mediaType == MediaType.APPLICATION_XML
                        ? response.bodyToMono(TireChangeTime.XmlResponse.class).map(TireChangeTime.XmlResponse::toList)
                        : response.bodyToMono(TireChangeTime[].class).map(List::of));
    }

    // Бронирование времени
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

    // Мастерская по названию города
    private TireShopConfig.TireShop getShopByCity(String city) {
        return tireShopConfig.getShops().stream()
                .filter(s -> s.getName().equalsIgnoreCase(city))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown city"));
    }

    // Определения типа контента - JSON или XML
    private MediaType getMediaType(TireShopConfig.TireShop shop) {
        return shop.getFormat().equalsIgnoreCase("xml") ? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;
    }
}