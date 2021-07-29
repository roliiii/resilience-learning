package com.fluffy.resilience.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class FakeServiceImpl implements FakeService {

    /**
     *
     * TIME LIMITER
     *
     */
    @Override
    @TimeLimiter(name = "fluffyTimeLimiter", fallbackMethod = "fallBackForSlowFakeService")
    public CompletableFuture<String> slowFakeService(boolean slow) throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> {

            if (slow){
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

    public CompletableFuture<String> fallBackForSlowFakeService(boolean slow, TimeoutException e){
        return CompletableFuture.completedFuture("I didn't wanted to wait that much...");
    }


    /**
     *
     * RETRY
     *
     */
    public static AtomicInteger reaminingDies = new AtomicInteger(0);

    @Override
    @Retry(name = "fluffyRetry", fallbackMethod = "fallBackForTry")
    public String dieable() throws Exception {

        if (reaminingDies.getAndDecrement() > 0){
            System.out.println("Died!!!");
            throw new Exception();
        }
        return "Yeah, resp from fakeService";
    }

    public String fallBackForTry(Throwable e){
        return "No life left!";
    }

}
