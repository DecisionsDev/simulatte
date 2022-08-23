package com.ibm.simulatte.online.controllers.simulation;

import com.ibm.simulatte.core.dto.SimulationDTO;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.services.simulation.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@EnableAutoConfiguration
@RestController
@RequestMapping("v1/simulation")
@Tag(name = "Simulation", description = "Simulation API")
public class SimulationController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    HttpServletRequest request ;

    @Autowired
    HttpServletResponse response ;
    @Autowired
    private SimulationService simulationService;


    @GetMapping("/")
    public ResponseEntity<List<Simulation>> getAllSimulationsRuns() {
        int userUid =  (int)request.getAttribute("uid") ;
        return ResponseEntity.ok(simulationService.getAllSimulations(userUid));
    }

    @Operation(
            summary = "Create simulation",
            description = "Create a simulation object from user inputs elements",
            tags = { "Simulation" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create a successful simulation object"),
            @ApiResponse(responseCode = "404", description = "Fail to create simulation object") })
    @PostMapping("/create")
    public ResponseEntity<Simulation> createSimulation(@RequestBody SimulationDTO newSimulationDTO) {
        Simulation newSimulation = convertDtoToEntity(newSimulationDTO);
        return ResponseEntity.ok(simulationService.create(newSimulation));
    }

    @GetMapping("/{simulationUid}")
    public ResponseEntity<Simulation> getSimulation(@PathVariable("simulationUid") int simulationUid){
        return ResponseEntity.ok(simulationService.findMatchedSimulation(simulationUid));
    }

    @Operation(
            summary = "Update simulation specification",
            description = "Update simulation specification by simulation uid",
            tags = { "Simulation" })
    @PutMapping("/update/{simulationUid}")
    public ResponseEntity<Simulation> updateSimulation(
            @PathVariable("simulationUid") int simulationUid,
            @RequestBody SimulationDTO updatedSimulationDTO
    ){
        if(!simulationService.hasBeenCreated(simulationUid))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Simulation with Uid = "+simulationUid+" doesn't exist !");
        Simulation updatedSimulation = convertDtoToEntity(updatedSimulationDTO);
        updatedSimulation.setUid(simulationUid);
        return ResponseEntity.ok(simulationService.update(updatedSimulation));
    }

    @GetMapping("/about")
    public ResponseEntity<String> landingPage(){
        System.out.println("SIMULATTE DESCRIPTION");
        return ResponseEntity.ok("<h1>SIMULATTE</h1>");
    }

    private Simulation convertDtoToEntity(SimulationDTO simulationDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(simulationDto, Simulation.class);
    }

}
