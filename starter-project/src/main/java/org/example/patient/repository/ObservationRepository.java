package org.example.patient.repository;

import org.example.patient.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
	List<Observation> findByEncounterId(Long encounterId);
	List<Observation> findByPatientId(Long patientId);
}
