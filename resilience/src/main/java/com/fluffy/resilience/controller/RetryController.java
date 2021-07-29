package com.fluffy.resilience.controller;


import com.fluffy.resilience.service.FakeService;
import com.fluffy.resilience.service.FakeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/retry")
public class RetryController {

    private FakeService fakeService;


    @GetMapping
    public String retry(@RequestParam(required = false, defaultValue = "0") int dieCount) throws Exception {
        FakeServiceImpl.reaminingDies.set(dieCount);
        return fakeService.dieable();
    }

}
