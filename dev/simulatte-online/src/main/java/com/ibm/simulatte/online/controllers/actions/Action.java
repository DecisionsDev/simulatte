package com.ibm.simulatte.online.controllers.actions;

import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.user.User;
import com.ibm.simulatte.core.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@RestController
@RequestMapping("v1/actions")
@Tag(name = "Action", description = "Action API")
public class Action {

    @Autowired


    @Operation(
            summary = "Delete simulation spec",
            description = "Delete simulation description",
            tags = { "Action" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Simulation description is deleted!"),
            @ApiResponse(responseCode = "404", description = "Fail to delete simulation description") })
    @DeleteMapping("/simulation/{uid}")
    public ResponseEntity<String> deleteSimulationMetadata(@PathVariable("uid") int uid) {

        return ResponseEntity.ok("Simulation "+uid+" is deleted.");
    }


    @Operation(
            summary = "Delete all simulation spec",
            description = "Delete all simulation description",
            tags = { "Action" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All simulation descriptions are deleted!"),
            @ApiResponse(responseCode = "404", description = "Fail to delete all the simulation description") })
    @DeleteMapping("/simulation/all")
    public ResponseEntity<String> resetDatabase() {
        return ResponseEntity.ok("Fail to delete all.");
    }

}
