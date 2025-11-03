package org.example.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.patient.model.Patient;
import org.example.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PatientService patientService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void testCreatePatient() throws Exception {
		Patient patient = new Patient();
		patient.setId(1L);
		patient.setIdentifier("P123");
		patient.setGivenName("John");
		patient.setFamilyName("Doe");
		patient.setBirthDate(new Date("1980-01-01"));
		patient.setGender("M");
		
		Mockito.when(patientService.createPatient(any(Patient.class))).thenReturn(patient);
		
		mockMvc.perform(post("/api/patients")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(patient)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.identifier").value("P123"))
				.andExpect(jsonPath("$.givenName").value("John"));
	}
	
	@Test
	void testGetPatientById_found() throws Exception {
		Patient patient = new Patient();
		patient.setId(1L);
		patient.setIdentifier("P123");
		
		Mockito.when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));
		
		mockMvc.perform(get("/api/patients/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.identifier").value("P123"));
	}
	
	@Test
	void testGetPatientById_notFound() throws Exception {
		Mockito.when(patientService.getPatientById(1L)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/api/patients/1"))
				.andExpect(status().isNotFound());
	}
}
