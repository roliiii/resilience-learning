package com.fluffy.resilience.controller;

import com.fluffy.resilience.service.FakeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
@RequestMapping("/timelimited")
public class TimeLimitController {

    private FakeService fakeService;

    @GetMapping
    public String timeLimited(@RequestParam(required = false, defaultValue = "false") Boolean slow) throws InterruptedException, ExecutionException {
        return fakeService.slowFakeService(slow).get();
    }

}
