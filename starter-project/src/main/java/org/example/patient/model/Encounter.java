package org.example.patient.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "encounters")
public class Encounter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	// Many encounters belong to one patient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patient;
	
	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	@NotBlank(message = "Encounter class is required")
	private String encounterClass;
	
	// One encounter has many observations
	@OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Observation> observations;
}
