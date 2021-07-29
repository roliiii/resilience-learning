package com.fluffy.resilience.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.concurrent.CompletableFuture;

public interface FakeService {
    //@TimeLimiter("")
    CompletableFuture<String> slowFakeService(boolean slow) throws InterruptedException;

    @Retry(name = "fluffyTimeLimiter", fallbackMethod = "fallBackForTry")
    String dieable() throws Exception;

    @CircuitBreaker(name = "fluffyRetry", fallbackMethod = "fallBackForCircuitBreaker")
    String dieable(boolean die) throws Exception;

    String resourceHungryService() throws InterruptedException;

    @Bulkhead(name = "fluffyBulkheadThreaded", fallbackMethod = "fallBackForResourceHungryService", type = Bulkhead.Type.THREADPOOL)
    CompletableFuture<String> resourceHungryService_2() throws InterruptedException;
}
