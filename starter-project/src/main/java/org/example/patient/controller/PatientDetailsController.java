package org.example.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.patient.model.Encounter;
import org.example.patient.model.Observation;
import org.example.patient.repository.EncounterRepository;
import org.example.patient.repository.ObservationRepository;
import org.example.patient.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/patient/{id}")
@Tag(name = "Patients", description = "Operations related to Patient details, Encounters, and Observations")
public class PatientDetailsController {
	
	private final PatientRepository patientRepo;
	private final EncounterRepository encounterRepo;
	private final ObservationRepository observationRepo;
	
	public PatientDetailsController(PatientRepository patientRepo,
			EncounterRepository encounterRepo,
			ObservationRepository observationRepo) {
		this.patientRepo = patientRepo;
		this.encounterRepo = encounterRepo;
		this.observationRepo = observationRepo;
	}
	
	// GET /api/patient/{id}/encounter
	@GetMapping("/encounter")
	@Operation(summary = "Get all encounters for a specific patient")
	public ResponseEntity<List<Encounter>> getEncounters(@PathVariable Long id) {
		if (!patientRepo.existsById(id)) return ResponseEntity.notFound().build();
		List<Encounter> encounters = encounterRepo.findByPatientId(id);
		return ResponseEntity.ok(encounters);
	}
	
	// GET /api/patient/{id}/observation
	@GetMapping("/observation")
	@Operation(summary = "Get all observations for a specific patient")
	public ResponseEntity<List<Observation>> getObservations(@PathVariable Long id) {
		if (!patientRepo.existsById(id)) return ResponseEntity.notFound().build();
		List<Observation> observations = observationRepo.findByPatientId(id);
		return ResponseEntity.ok(observations);
	}
	
	// POST /api/patient/{id}/encounter
	@PostMapping("/encounter")
	@Operation(summary = "Create a new encounter for a specific patient")
	public ResponseEntity<Encounter> createEncounter(
			@Valid
			@PathVariable Long id,
			@RequestBody Encounter encounterRequest) {
		
		return patientRepo.findById(id).map(patient -> {
			encounterRequest.setPatient(patient);
			Encounter saved = encounterRepo.save(encounterRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);
		}).orElse(ResponseEntity.notFound().build());
	}
	
	// POST /api/patient/{id}/observation
	@PostMapping("/observation")
	@Operation(summary = "Create a new observation for a specific patient")
	public ResponseEntity<Observation> createObservation(
			@Valid
			@PathVariable Long id,
			@RequestBody Observation observationRequest) {
		
		return patientRepo.findById(id).map(patient -> {
			observationRequest.setPatient(patient);
			if (observationRequest.getEncounter() != null && observationRequest.getEncounter().getId() != null) {
				encounterRepo.findById(observationRequest.getEncounter().getId())
						.ifPresent(observationRequest::setEncounter);
			}
			Observation saved = observationRepo.save(observationRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);
		}).orElse(ResponseEntity.notFound().build());
	}
}
