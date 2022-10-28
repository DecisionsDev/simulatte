package com.ibm.simulatte.core.repositories.simulation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SimulationRepositoryTest {
    @Autowired
    private SimulationRepository simulationRepositoryTest;


    @AfterEach
    void tearDown() {
        simulationRepositoryTest.deleteAll();
    }

    @Test
    void itShouldFindSimulationByUid() {
    }

    @Test
    void itShouldFindAllSimulation() {
    }

    @Test
    void findAllByUserUid() {
    }

    @Test
    void existsByUid() {
    }
}