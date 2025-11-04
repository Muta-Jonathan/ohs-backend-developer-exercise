package org.example.patient.repository;

import org.example.patient.model.Encounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
	Page<Encounter> findByPatientId(Long patientId, Pageable pageable);
	Page<Encounter> findByPatientIdAndStartTimeBetween(Long patientId, Date start, Date end, Pageable pageable);
}
