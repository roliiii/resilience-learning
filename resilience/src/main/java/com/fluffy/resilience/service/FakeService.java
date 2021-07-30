package com.fluffy.resilience.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.concurrent.CompletableFuture;

public interface FakeService {
    //@TimeLimiter("")
    CompletableFuture<String> slowFakeService(boolean slow) throws InterruptedException;

    String dieable();

    String dieable(boolean die) throws Exception;

    String resourceHungryService() throws InterruptedException;

    CompletableFuture<String> resourceHungryService_2() throws InterruptedException;
}
