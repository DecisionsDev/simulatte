package com.ibm.simulatte.core.repositories.simulation;

import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Integer>{

    Simulation findByUid(int uid);

    //List<Simulation> findAllBy;
    List<Simulation> findAllBy();

    List<Simulation> findAllByUserUid(int userUid);

    boolean existsByUid(int simulationUid);
}
