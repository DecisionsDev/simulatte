package com.ibm.simulatte.online.controllers.optimization;

import com.ibm.simulatte.core.dto.OptimizedSimulationDTO;
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
@RequestMapping("v1/optimization")
@Tag(name = "Optimisation", description = "Simulation with Optimization API")
public class OptimizationController {

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
            summary = "Create simulation for optimization",
            description = "Create a simulation object from user inputs elements",
            tags = { "Simulation" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create a successful simulation object"),
            @ApiResponse(responseCode = "404", description = "Fail to create simulation object") })
    @PostMapping("/create")
    public ResponseEntity<Simulation> createSimulation(@RequestBody OptimizedSimulationDTO newOptimizedSimulationDTO) {
        Simulation newSimulation = convertDtoToEntity(newOptimizedSimulationDTO);
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
            @RequestBody OptimizedSimulationDTO updatedOptimizedSimulationDTO
    ){
        if(!simulationService.hasBeenCreated(simulationUid))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Simulation with Uid = "+simulationUid+" doesn't exist !");
        Simulation updatedSimulation = convertDtoToEntity(updatedOptimizedSimulationDTO);
        updatedSimulation.setUid(simulationUid);
        return ResponseEntity.ok(simulationService.update(updatedSimulation));
    }

    @GetMapping("/about")
    public ResponseEntity<String> landingPage(){
        return ResponseEntity.ok("<h1>SimuLatte for optimisation</h1>");
    }

    private Simulation convertDtoToEntity(OptimizedSimulationDTO optimizedSimulationDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(optimizedSimulationDto, Simulation.class);
    }

}
