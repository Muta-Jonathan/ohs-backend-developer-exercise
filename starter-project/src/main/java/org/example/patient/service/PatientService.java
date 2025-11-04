package org.example.patient.service;

import org.example.patient.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
	
	Patient createPatient(Patient patient);
	
	Optional<Patient> getPatientById(Long id);
	
	Patient updatePatient(Long id, Patient updatedPatient);
	
	void deletePatient(Long id);
	
	List<Patient> searchPatients(String family, String given, String identifier, String birthDate);
	
	List<Patient> getAllPatients();
}
