package org.example.patient.service;

import org.example.patient.model.Patient;
import org.example.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
	
	private final PatientRepository patientRepository;
	
	public PatientServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}
	
	@Override
	public Patient createPatient(Patient patient) {
		return patientRepository.save(patient);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Patient> getPatientById(Long id) {
		return patientRepository.findById(id);
	}
	
	@Override
	public Patient updatePatient(Long id, Patient updatedPatient) {
		// find existing patient and update fields
		return patientRepository.findById(id)
				.map(existing -> {
					existing.setGivenName(updatedPatient.getGivenName());
					existing.setFamilyName(updatedPatient.getFamilyName());
					existing.setBirthDate(updatedPatient.getBirthDate());
					existing.setGender(updatedPatient.getGender());
					existing.setIdentifier(updatedPatient.getIdentifier());
					return patientRepository.save(existing);
				}).orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
	}
	
	@Override
	public void deletePatient(Long id) {
		patientRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Patient> searchPatients(String family, String given, String identifier, String birthDate) {
		// fetch all patients and filter in memory
		List<Patient> allPatients = patientRepository.findAll();
		
		return allPatients.stream()
				.filter(p -> family == null || p.getFamilyName().equals(family))
				.filter(p -> given == null || p.getGivenName().equals(given))
				.filter(p -> identifier == null || p.getIdentifier().equals(identifier))
				.filter(p -> birthDate == null || p.getBirthDate().toString().equals(birthDate))
				.toList();
	}
}
