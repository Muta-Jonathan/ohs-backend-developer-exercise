package org.example.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
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
	private String identifier;
	
	private String givenName;
	
	private String familyName;
	
	private Date birthDate;
	
	@Pattern(regexp = "M|F", message = "Gender must be Male (M), Female (F)")
	private String gender;
	
}
