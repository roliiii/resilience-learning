package com.fluffy.resilience.service;

import io.github.resilience4j.retry.annotation.Retry;

import java.util.concurrent.CompletableFuture;

public interface FakeService {
    //@TimeLimiter("")
    CompletableFuture<String> slowFakeService(boolean slow) throws InterruptedException;

    @Retry(name = "fluffyTimeLimiter", fallbackMethod = "fallBackForTry")
    String dieable() throws Exception;
}