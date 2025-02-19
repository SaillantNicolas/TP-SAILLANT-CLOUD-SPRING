package com.medical.patient_service.service;

import com.medical.patient_service.model.Patient;
import com.medical.patient_service.repository.PatientRepository;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private static final String PATIENT_SERVICE = "patientService";

    @CircuitBreaker(name = PATIENT_SERVICE, fallbackMethod = "getAllPatientsFallback")
    @Retry(name = PATIENT_SERVICE)
    public List<Patient> getAllPatients() {
        log.info("Getting all patients");
        return patientRepository.findAll();
    }

    public List<Patient> getAllPatientsFallback(Exception e) {
        log.error("Fallback: Error getting all patients", e);
        return new ArrayList<>();
    }

    @CircuitBreaker(name = PATIENT_SERVICE, fallbackMethod = "getPatientByIdFallback")
    @Retry(name = PATIENT_SERVICE)
    public Optional<Patient> getPatientById(Long id) {
        log.info("Getting patient with id: {}", id);
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByIdFallback(Long id, Exception e) {
        log.error("Fallback: Error getting patient with id: {}", id, e);
        return Optional.empty();
    }

    @CircuitBreaker(name = PATIENT_SERVICE, fallbackMethod = "createPatientFallback")
    @Retry(name = PATIENT_SERVICE)
    public Patient createPatient(Patient patient) {
        log.info("Creating patient: {}", patient);
        return patientRepository.save(patient);
    }

    public Patient createPatientFallback(Patient patient, Exception e) {
        log.error("Fallback: Error creating patient", e);
        return new Patient();
    }

    @CircuitBreaker(name = PATIENT_SERVICE, fallbackMethod = "updatePatientFallback")
    @Retry(name = PATIENT_SERVICE)
    public Optional<Patient> updatePatient(Long id, Patient patient) {
        log.info("Updating patient with id: {}", id);
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    patient.setId(id);
                    return patientRepository.save(patient);
                });
    }

    public Optional<Patient> updatePatientFallback(Long id, Patient patient, Exception e) {
        log.error("Fallback: Error updating patient with id: {}", id, e);
        return Optional.empty();
    }

    @CircuitBreaker(name = PATIENT_SERVICE, fallbackMethod = "deletePatientFallback")
    @Retry(name = PATIENT_SERVICE)
    public void deletePatient(Long id) {
        log.info("Deleting patient with id: {}", id);
        patientRepository.deleteById(id);
    }

    public void deletePatientFallback(Long id, Exception e) {
        log.error("Fallback: Error deleting patient with id: {}", id, e);
    }
}