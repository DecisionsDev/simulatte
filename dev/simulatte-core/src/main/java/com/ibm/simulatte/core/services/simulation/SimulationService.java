package com.ibm.simulatte.core.services.simulation;

import com.ibm.simulatte.core.datamodels.decisionService.Type;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.repositories.simulation.SimulationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class SimulationService {

    private final SimulationRepository simulationRepository;

    public SimulationService(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }

    public Simulation create(Simulation newSimulation) {
        //implement required attributes here
        if(newSimulation.getName()==null || newSimulation.getName().trim().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation name is empty.");
        }

        if(newSimulation.getDecisionService().getType() == null ||
                !Type.isValidType(newSimulation.getDecisionService().getType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, check your decision service type.");
        }

        //simulation endPoint validator
        /*if(!new UrlValidator()
                .isValid(newSimulation
                        .getDecisionService()
                        .getEndPoint())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please, check your decision service endPoint.");
        }*/

        if(!newSimulation.getTrace()) {
            log.info("Your simulation will not get the TRACE!");
        }

        return simulationRepository.save(newSimulation);
    }

    public Simulation update(Simulation updatedSimulation) {
        return simulationRepository.save(updatedSimulation);
    }

    public boolean hasBeenCreated(int simulationUid) {
        return simulationRepository.existsByUid(simulationUid);
    }

    public Simulation findMatchedSimulation(int simulationUid) {
        return simulationRepository.findByUid(simulationUid);
    }

    public List<Simulation> getAllSimulations(int userUid) {
        return simulationRepository.findAllByUserUid(userUid);
    }
}
