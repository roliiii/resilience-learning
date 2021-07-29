package com.fluffy.resilience.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratelimited")
public class RateLimitedController {

    @GetMapping
    @RateLimiter(name = "fluffyRateLimiter", fallbackMethod = "fallback")
    public String ratelimited(){
        return "Okay, we have the power :)";
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public String fallback(RuntimeException e){
        return "this is too much for me!";
    }

}
