package com.example.tire_change.controller;


import com.example.tire_change.dto.TireChangeTime;
import com.example.tire_change.service.TireChangeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import java.util.List;

@RestController
@RequestMapping("/api/tire-change")

public class TireChangeController {
    private final TireChangeService tireChangeService;

    @Autowired
    public TireChangeController(TireChangeService tireChangeService) {
        this.tireChangeService = tireChangeService;
    }

    @GetMapping("/available")
    public Mono<List<TireChangeTime>> getAvailableTimes(@RequestParam String city, @RequestParam String from, @RequestParam String until) {
        return tireChangeService.getAvailableTimes(city, from, until);
    }

    @PostMapping("/book")
    public Mono<String> bookTireChange(@RequestBody TireChangeTime.BookingRequest request) {
        System.out.println("City: " + request.getCity());
        System.out.println("ID: " + request.getId());
        System.out.println("Contact: " + request.getContactInformation());

        return tireChangeService.bookTireChange(request.getCity(), request.getId(), request.getContactInformation());
    }

}