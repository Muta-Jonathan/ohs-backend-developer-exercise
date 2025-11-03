package org.example.patient.service;

import org.example.patient.model.Patient;
import org.example.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PatientServiceImplTest {
	
	private PatientRepository patientRepository;
	private PatientServiceImpl patientService;
	
	@BeforeEach
	public void setUp() {
		patientRepository = mock(PatientRepository.class);
		patientService = new PatientServiceImpl(patientRepository);
	}
	
	@Test
	public void testCreatePatient() {
		Patient patient = new Patient();
		patient.setIdentifier("P123");
		patient.setGivenName("John");
		patient.setFamilyName("Doe");
		
		when(patientRepository.save(patient)).thenReturn(patient);
		
		Patient created = patientService.createPatient(patient);
		assertNotNull(created);
		assertEquals("P123", created.getIdentifier());
		verify(patientRepository, times(1)).save(patient);
	}
	
	@Test
	public void testGetPatientById_found() {
		Patient patient = new Patient();
		patient.setId(1L);
		patient.setIdentifier("P123");
		
		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
		
		Optional<Patient> found = patientService.getPatientById(1L);
		assertTrue(found.isPresent());
		assertEquals("P123", found.get().getIdentifier());
	}
	
	@Test
	public void testSearchPatients_filtersByFamilyName() {
		Patient p1 = new Patient();
		p1.setFamilyName("Doe");
		Patient p2 = new Patient();
		p2.setFamilyName("Smith");
		
		when(patientRepository.findAll()).thenReturn(List.of(p1, p2));
		
		List<Patient> results = patientService.searchPatients("Doe", null, null, null);
		assertEquals(1, results.size());
		assertEquals("Doe", results.get(0).getFamilyName());
	}
}
