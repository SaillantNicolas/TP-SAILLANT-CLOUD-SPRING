package com.medical.gateway_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/patient")
    public Mono<Map<String, String>> patientServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Patient service is currently unavailable. Please try again later.");
        return Mono.just(response);
    }

    @RequestMapping("/practitioner")
    public Mono<Map<String, String>> practitionerServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Practitioner service is currently unavailable. Please try again later.");
        return Mono.just(response);
    }
}