package com.example.tire_change.controller;


import com.example.tire_change.dto.TireChangeTime;
import com.example.tire_change.service.TireChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import java.util.List;

@RestController
@RequestMapping("/api/tire-change")

public class TireChangeController {
    private final TireChangeService service;

    @Autowired
    public TireChangeController(TireChangeService service) {
        this.service = service;
    }

    @GetMapping("/available")
    public Mono<List<TireChangeTime>> getAvailableTimes(@RequestParam String city, @RequestParam String from, @RequestParam String until) {
        return service.getAvailableTimes(city, from, until);
    }

    @PostMapping("/book")
    public Mono<String> bookTireChange(
            @RequestParam(name = "city", defaultValue = "unknown") String city,
            @RequestParam(name = "id", defaultValue = "0") String id,
            @RequestParam(name = "contact", defaultValue = "no_contact") String contact) {

        System.out.println("City: " + city);
        System.out.println("ID: " + id);
        System.out.println("Contact: " + contact);

        return service.bookTireChange(city, id, contact);
    }
}