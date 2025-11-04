package org.example.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.patient.model.Patient;
import org.example.patient.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@Validated
@Tag(name = "Patients", description = "CRUD operations for Patients")
public class PatientController {
	
	private final PatientService patientService;
	
	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}
	
	// POST /api/patient
	@PostMapping
	@Operation(summary = "Create a new patient")
	public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
		Patient saved = patientService.createPatient(patient);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	// GET /api/patient/{id}
	@GetMapping("/{id}")
	@Operation(summary = "Get patient by ID")
	public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
		return patientService.getPatientById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	// PUT /api/patient/{id}
	@PutMapping("/{id}")
	@Operation(summary = "Update an existing patient")
	public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
			@Valid @RequestBody Patient patient) {
		try {
			Patient updated = patientService.updatePatient(id, patient);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	// DELETE /api/patient/{id}
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a patient by ID")
	public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
		patientService.deletePatient(id);
		return ResponseEntity.noContent().build();
	}
	
	// GET /api/patient?family=&given=&identifier=&birthDate=
	@GetMapping
	@Operation(summary = "Search patients by optional parameters")
	public ResponseEntity<List<Patient>> searchPatients(
			@RequestParam(required = false) String family,
			@RequestParam(required = false) String given,
			@RequestParam(required = false) String identifier,
			@RequestParam(required = false) String birthDate
	) {
		List<Patient> results = patientService.searchPatients(family, given, identifier, birthDate);
		return ResponseEntity.ok(results);
	}
	
	// GET /api/patient/all
	@GetMapping("/all")
	@Operation(summary = "Get all patients")
	public ResponseEntity<List<Patient>> getAllPatients() {
		List<Patient> patients = patientService.getAllPatients();
		return ResponseEntity.ok(patients);
	}
}
