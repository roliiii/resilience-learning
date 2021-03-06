package com.fluffy.resilience.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
@Slf4j
@AllArgsConstructor
public class FakeServiceImpl implements FakeService {

    private RetryRegistry retryRegistry;

    /**
     * TIME LIMITER
     */
    @Override
    @TimeLimiter(name = "fluffyTimeLimiter", fallbackMethod = "fallBackForSlowFakeService")
    public CompletableFuture<String> slowFakeService(boolean slow) {
        return CompletableFuture.supplyAsync(() -> {

            if (slow) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("I am finished");

            return "I'm the result of an imaginary service call!";
        });
    }

    public CompletableFuture<String> fallBackForSlowFakeService(boolean slow, TimeoutException e) {
        return CompletableFuture.completedFuture("I didn't wanted to wait that much...");
    }


    /**
     * RETRY
     */
    public static AtomicInteger remainingDies = new AtomicInteger(0);


    //@Override
    //@Retry(name = "fluffyRetry", fallbackMethod = "fallBackForTry")
    private String dieableFunc() {

        if (remainingDies.getAndDecrement() > 0) {
            System.out.println("Died!!!");
            throw new RuntimeException();
        }
        return "I'm the result of an imaginary service call!";
    }


    @Override
    public String dieable() {
        Retry fluffyRetry = retryRegistry.retry("fluffyRetry");
        Supplier<String> stringSupplier =
                Decorators.ofSupplier(this::dieableFunc)
                        .withRetry(fluffyRetry)
                        .withFallback(this::fallBackForTry)
                        .decorate();

        return stringSupplier.get();
    }

    public String fallBackForTry(Throwable e) {
        return "No life left!";
    }


    /**
     * CircuitBreaker
     */
    @Override
    @CircuitBreaker(name = "fluffyCircuitBreaker", fallbackMethod = "fallBackForCircuitBreaker")
    public String dieable(boolean die) throws Exception {
        System.out.println("I am called!!!");

        if (die) {
            throw new Exception();
        }

        return "I'm the result of an imaginary service call!";
    }

    public String fallBackForCircuitBreaker(boolean die, Throwable e) {
        return "fallback!";
    }

    /**
     * bulkhead
     */
    @Override
    @Bulkhead(name = "fluffyBulkhead", fallbackMethod = "fallBackForResourceHungryService")
    public String resourceHungryService() throws InterruptedException {
        Thread.sleep(5000);
        return "I'm the result of an imaginary service call!";
    }

    public String fallBackForResourceHungryService(Throwable e) {
        return "fallback!";
    }

    @Override
    @Bulkhead(name = "fluffyBulkheadThreaded", fallbackMethod = "fallBackForResourceHungryService_2", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> resourceHungryService_2() throws InterruptedException {

        return CompletableFuture.supplyAsync(() -> {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "I'm the result of an imaginary service call!";
        });

    }

    public CompletableFuture<String> fallBackForResourceHungryService_2(Throwable e) {
        return CompletableFuture.completedFuture("fallback!");
    }

}
