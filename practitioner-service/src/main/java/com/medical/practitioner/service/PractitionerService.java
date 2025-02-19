package com.medical.practitioner.service;

import com.medical.practitioner.model.Practitioner;
import com.medical.practitioner.repository.PractitionerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PractitionerService {

    private final PractitionerRepository practitionerRepository;
    private static final String PRACTITIONER_SERVICE = "practitionerService";

    @CircuitBreaker(name = PRACTITIONER_SERVICE, fallbackMethod = "getAllPractitionersFallback")
    @Retry(name = PRACTITIONER_SERVICE)
    public List<Practitioner> getAllPractitioners() {
        log.info("Getting all practitioners");
        return practitionerRepository.findAll();
    }

    public List<Practitioner> getAllPractitionersFallback(Exception e) {
        log.error("Fallback: Error getting all practitioners", e);
        return new ArrayList<>();
    }

    @CircuitBreaker(name = PRACTITIONER_SERVICE, fallbackMethod = "getPractitionerByIdFallback")
    @Retry(name = PRACTITIONER_SERVICE)
    public Optional<Practitioner> getPractitionerById(Long id) {
        log.info("Getting practitioner with id: {}", id);
        return practitionerRepository.findById(id);
    }

    public Optional<Practitioner> getPractitionerByIdFallback(Long id, Exception e) {
        log.error("Fallback: Error getting practitioner with id: {}", id, e);
        return Optional.empty();
    }

    @CircuitBreaker(name = PRACTITIONER_SERVICE, fallbackMethod = "createPractitionerFallback")
    @Retry(name = PRACTITIONER_SERVICE)
    public Practitioner createPractitioner(Practitioner practitioner) {
        log.info("Creating practitioner: {}", practitioner);
        return practitionerRepository.save(practitioner);
    }

    public Practitioner createPractitionerFallback(Practitioner practitioner, Exception e) {
        log.error("Fallback: Error creating practitioner", e);
        return new Practitioner();
    }

    @CircuitBreaker(name = PRACTITIONER_SERVICE, fallbackMethod = "updatePractitionerFallback")
    @Retry(name = PRACTITIONER_SERVICE)
    public Optional<Practitioner> updatePractitioner(Long id, Practitioner practitioner) {
        log.info("Updating practitioner with id: {}", id);
        return practitionerRepository.findById(id)
                .map(existingPractitioner -> {
                    practitioner.setId(id);
                    return practitionerRepository.save(practitioner);
                });
    }

    public Optional<Practitioner> updatePractitionerFallback(Long id, Practitioner practitioner, Exception e) {
        log.error("Fallback: Error updating practitioner with id: {}", id, e);
        return Optional.empty();
    }

    @CircuitBreaker(name = PRACTITIONER_SERVICE, fallbackMethod = "deletePractitionerFallback")
    @Retry(name = PRACTITIONER_SERVICE)
    public void deletePractitioner(Long id) {
        log.info("Deleting practitioner with id: {}", id);
        practitionerRepository.deleteById(id);
    }

    public void deletePractitionerFallback(Long id, Exception e) {
        log.error("Fallback: Error deleting practitioner with id: {}", id, e);
    }
}