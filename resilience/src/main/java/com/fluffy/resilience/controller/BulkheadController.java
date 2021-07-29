package com.fluffy.resilience.controller;


import com.fluffy.resilience.service.FakeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
@RequestMapping("/bulkhead")
public class BulkheadController {

    private FakeService fakeService;

    @GetMapping()
    public String bulkhead() throws InterruptedException {
        return fakeService.resourceHungryService();
    }

    @GetMapping("/threadpool")
    public String bulkheadThreadPool() throws InterruptedException, ExecutionException {
        return fakeService.resourceHungryService_2().get();
    }

}
