package org.example.patient.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "patient")
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	@Column(nullable = false, unique = true)
	@NotBlank(message = "Patient identifier is required")
	private String identifier;
	
	@NotBlank(message = "Given name is required")
	private String givenName;
	
	private String familyName;
	
	@Past(message = "Birth date must be in the past")
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	@Pattern(regexp = "M|F|U", message = "Gender must be Male (M), Female (F), or Unknown (U)")
	private String gender;
	
	// A patient can have multiple encounters
	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "patient-encounters")
	private List<Encounter> encounters;
	
	// A patient can have multiple observations
	@OneToMany(mappedBy = "patient",cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference(value = "patient-observations")
	private List<Observation> observations;
}
