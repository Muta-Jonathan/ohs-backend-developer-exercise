package org.example.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.patient.model.Encounter;
import org.example.patient.model.Observation;
import org.example.patient.repository.EncounterRepository;
import org.example.patient.repository.ObservationRepository;
import org.example.patient.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{id}")
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
	
	/**
	 * GET /api/patients/{id}/encounters
	 * @param id
	 * @param sortBy default "startTime"
	 * @param page default 0
	 * @param size default 10
	 * @param startDate not required
	 * @param endDate not required
	 * @return
	 */
	@GetMapping("/encounters")
	@Operation(summary = "Get all encounters for a specific patient")
	public ResponseEntity<Page<Encounter>> getEncounters(
			@PathVariable Long id,
			@RequestParam(defaultValue = "startTime") String sortBy,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
	) {
		if (!patientRepo.existsById(id)) return ResponseEntity.notFound().build();
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		
		Page<Encounter> encounters;
		if (startDate != null && endDate != null) {
			encounters = encounterRepo.findByPatientIdAndStartTimeBetween(id, startDate, endDate, pageable);
		} else {
			encounters = encounterRepo.findByPatientId(id, pageable);
		}
		return ResponseEntity.ok(encounters);
	}
	
	// GET /api/patients/{id}/observations
	@GetMapping("/observations")
	@Operation(summary = "Get all observations for a specific patient")
	public ResponseEntity<List<Observation>> getObservations(@PathVariable Long id) {
		if (!patientRepo.existsById(id)) return ResponseEntity.notFound().build();
		List<Observation> observations = observationRepo.findByPatientId(id);
		return ResponseEntity.ok(observations);
	}
	
	// POST /api/patients/{id}/encounters
	@PostMapping("/encounters")
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
	
	// POST /api/patients/{id}/observations
	@PostMapping("/observations")
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
