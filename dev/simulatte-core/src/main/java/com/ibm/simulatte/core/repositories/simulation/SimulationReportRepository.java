package com.ibm.simulatte.core.repositories.simulation;

import com.ibm.simulatte.core.datamodels.simulation.SimulationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationReportRepository extends JpaRepository<SimulationReport, Integer> {
}
