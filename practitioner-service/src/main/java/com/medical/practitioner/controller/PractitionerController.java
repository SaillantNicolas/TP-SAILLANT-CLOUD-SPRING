package com.medical.practitioner.controller;

import com.medical.practitioner.model.Practitioner;
import com.medical.practitioner.service.PractitionerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practitioners")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Practitioner Management", description = "API endpoints for managing medical practitioners")
public class PractitionerController {

    private final PractitionerService practitionerService;

    @GetMapping
    @Operation(summary = "Get all practitioners", description = "Retrieves a list of all practitioners in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all practitioners",
            content = @Content(schema = @Schema(implementation = Practitioner.class)))
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        log.info("Request received to get all practitioners");
        List<Practitioner> practitioners = practitionerService.getAllPractitioners();
        return ResponseEntity.ok(practitioners);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get practitioner by ID", description = "Retrieves a specific practitioner by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the practitioner",
                    content = @Content(schema = @Schema(implementation = Practitioner.class))),
            @ApiResponse(responseCode = "404", description = "Practitioner not found",
                    content = @Content)
    })
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable Long id) {
        log.info("Request received to get practitioner with id: {}", id);
        return practitionerService.getPractitionerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new practitioner", description = "Adds a new practitioner to the system")
    @ApiResponse(responseCode = "201", description = "Practitioner successfully created",
            content = @Content(schema = @Schema(implementation = Practitioner.class)))
    public ResponseEntity<Practitioner> createPractitioner(@RequestBody Practitioner practitioner) {
        log.info("Request received to create a new practitioner: {}", practitioner);
        Practitioner createdPractitioner = practitionerService.createPractitioner(practitioner);
        return new ResponseEntity<>(createdPractitioner, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update practitioner", description = "Updates an existing practitioner's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Practitioner successfully updated",
                    content = @Content(schema = @Schema(implementation = Practitioner.class))),
            @ApiResponse(responseCode = "404", description = "Practitioner not found",
                    content = @Content)
    })
    public ResponseEntity<Practitioner> updatePractitioner(@PathVariable Long id, @RequestBody Practitioner practitioner) {
        log.info("Request received to update practitioner with id: {}", id);
        return practitionerService.updatePractitioner(id, practitioner)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a practitioner", description = "Removes a practitioner from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Practitioner successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Practitioner not found",
                    content = @Content)
    })
    public ResponseEntity<Void> deletePractitioner(@PathVariable Long id) {
        log.info("Request received to delete practitioner with id: {}", id);
        practitionerService.deletePractitioner(id);
        return ResponseEntity.noContent().build();
    }
}