package controller;

import dto.TireChangeTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.TireChangeService;

import java.util.List;

@RestController
@RequestMapping("/api/tire-change")
@RequiredArgsConstructor
public class TireChangeController {
    private final TireChangeService service;

    @GetMapping("/available")
    public Mono<List<TireChangeTime>> getAvailableTimes(@RequestParam String city, @RequestParam String from, @RequestParam String until) {
        return service.getAvailableTimes(city, from, until);
    }

    @PostMapping("/book")
    public Mono<String> bookTireChange(@RequestParam String city, @RequestParam String id, @RequestParam String contact) {
        return service.bookTireChange(city, id, contact);
    }
}