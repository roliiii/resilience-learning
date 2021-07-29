package com.fluffy.resilience.controller;

import com.fluffy.resilience.service.FakeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/circuitbreaker")
public class CircuitBreakerController {

    public FakeService fakeService;

    @GetMapping()
    public String circuitBreaker(@RequestParam(required = false, defaultValue = "false") Boolean fail) throws Exception {
        return fakeService.dieable(fail);
    }

}
