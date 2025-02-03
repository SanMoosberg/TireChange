package com.example.tire_change.service;
import com.example.tire_change.config.TireShopConfig;
import com.example.tire_change.dto.TireChangeTime;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.StringWriter;
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

        if (shop.getFormat().equalsIgnoreCase("xml")) {
            return getAvailableTimesForXml(shop, from, until);
        } else {
            return getAvailableTimesForJson(shop, from, until);
        }
    }

    private Mono<List<TireChangeTime>> getAvailableTimesForXml(TireShopConfig.TireShop shop, String from, String until) {
        return webClient.get()
                .uri(shop.getBaseUrl() + shop.getTimeEndpoint() + "?from=" + from + "&until=" + until)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(TireChangeTime.XmlResponse.class)
                .map(response -> response.toList());
    }

    private Mono<List<TireChangeTime>> getAvailableTimesForJson(TireShopConfig.TireShop shop, String from, String until) {
        return webClient.get()
                .uri(shop.getBaseUrl() + shop.getTimeEndpoint() + "?from=" + from + "&until=" + until)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TireChangeTime>>() {
                })
                .map(response -> response);
    }

    public Mono<String> bookTireChange(String city, String id, String contactInformation) {
        System.out.println("Book tire change called");
        System.out.println("city: " + city);
        System.out.println("id: " + id);
        System.out.println("contactInfo: " + contactInformation);

        TireShopConfig.TireShop shop = getShopByCity(city);
        System.out.println("shop: " + shop);

        MediaType mediaType = getMediaType(shop);
        HttpMethod httpMethod = shop.getFormat().equalsIgnoreCase("xml") ? HttpMethod.PUT : HttpMethod.POST;

        String bookingUrl = shop.getBaseUrl() + shop.getBookingEndpoint()
                .replace("{id}", id)
                .replace("{uuid}", id);

        Object requestBody;
        if (shop.getFormat().equalsIgnoreCase("xml")) {
            requestBody = convertToXml(new TireChangeTime.BookingRequest(contactInformation)); // Преобразуем в XML
        } else {
            requestBody = new TireChangeTime.BookingRequest(contactInformation); // JSON
        }

        return webClient.method(httpMethod)
                .uri(bookingUrl)
                .contentType(mediaType)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    // Метод для сериализации объекта в XML
    private String convertToXml(TireChangeTime.BookingRequest request) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TireChangeTime.BookingRequest.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(request, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Ошибка сериализации XML", e);
        }
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