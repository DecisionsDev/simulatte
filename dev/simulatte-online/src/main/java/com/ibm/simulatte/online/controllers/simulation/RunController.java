package com.ibm.simulatte.online.controllers.simulation;

import com.ibm.simulatte.core.dto.RunDTO;
import com.ibm.simulatte.core.datamodels.analytic.RunsComparison;
import com.ibm.simulatte.core.datamodels.run.RunAction;
import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.executions.online.OnlineServiceInvoker;
import com.ibm.simulatte.core.services.data.DataService;
import com.ibm.simulatte.core.services.run.RunReportService;
import com.ibm.simulatte.core.services.run.RunService;
import com.ibm.simulatte.core.services.simulation.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.ibm.simulatte.core.utils.DataManager.serializeToJSONArray;

@Slf4j
@EnableAutoConfiguration
@RestController
@RequestMapping("v1/simulations")
@Tag(name = "Run", description = "Simulation Run API")
public class RunController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    HttpServletRequest request ;

    @Autowired
    HttpServletResponse response ;

    @Autowired
    private RunService runService;

    @Autowired
    private RunReportService runReportService;
    @Autowired
    private SimulationService simulationService;

    @Autowired
    private DataService dataService;

    @Autowired
    private OnlineServiceInvoker onlineServiceInvoker;

    @GetMapping("{simulationUid}/runs/")
    public ResponseEntity<List<Run>> getAllSimulationRuns(@PathVariable("simulationUid") int simulationUid) {
        //int userUid =  (int)request.getAttribute("uid") ;
        return ResponseEntity.ok(runService.getAllRunsBySimulationUid(simulationUid));
    }

    @GetMapping("{simulationUid}/runs/{runUid}")
    public ResponseEntity<Run> getRun(@PathVariable("simulationUid") int simulationUid, @PathVariable("runUid") int runUid) {
        //int userUid =  (int)request.getAttribute("uid") ;
        return ResponseEntity.ok(runService.getRunBySimulationUid(simulationUid, runUid));
    }

    @PostMapping("{simulationUid}/start/")
    public ResponseEntity<Run> startSimulation(
            @PathVariable("simulationUid") int simulationUid,
            @RequestBody RunDTO newRunDTO) throws Exception {

        //all required fields checking here
        Simulation matchedSimulation = simulationService.findMatchedSimulation(simulationUid);
        if(matchedSimulation==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no simulation with uid = "+simulationUid+". Please check your simulation uid !"); //check if simulation exists

        //set nullable fields
        if(newRunDTO.getTrace()==null) newRunDTO.setTrace(matchedSimulation.getTrace());
        if(newRunDTO.getDecisionService()==null) newRunDTO.setDecisionService(matchedSimulation.getDecisionService());
        if(newRunDTO.getDataSink()==null) newRunDTO.setDataSink(matchedSimulation.getDataSink());

        Run newRun = convertDtoToEntity(newRunDTO); //mapping to run object
        newRun.setSimulationUid(simulationUid);
        return ResponseEntity.ok(runService.startToRun(newRun));
    }

    @Operation(
            summary = "Run actions",
            description = "You can PAUSE/CONTINUE/STOP a run execution or get info about his current state",
            tags = { "Run" })
    @PostMapping("{simulationUid}/runs/{runUid}/actions/{action}")
    public ResponseEntity<RunReport> doActionOnRun(
            @PathVariable("simulationUid") int simulationUid,
            @PathVariable("runUid") int runUid,
            @PathVariable("action") String action) throws Exception {
        if(simulationService.hasBeenCreated(simulationUid) && runService.hasBeenCreated(runUid)){
            RunReport currentRunReport = runReportService.getByRunReportUid(runUid);
            switch (RunAction.valueOf(action.toUpperCase())){
                case GET_REPORT:
                    return getRunReport(runUid, currentRunReport);
                case PAUSE:
                    return pauseRun(runUid, currentRunReport);
                case CONTINUE:
                    return continueRun(runUid, currentRunReport);
                case STOP:
                    return stopRun(runUid, currentRunReport);
                default:
                    log.error("Incorrect run action type. Please check your url !");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Incorrect run action type. Please check your url !");
            }
        }else{throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Simulation uid or Run uid doesn't exist!");}
    }

    @Operation(
            summary = "Run actions",
            description = "Compare two runs",
            tags = { "Run" })
    @PostMapping("runs/{firstRunUid}-vs-{secondRunUid}")
    public ResponseEntity<List<Run>> compare2Runs(
            @PathVariable("firstRunUid") int firstRunUid,
            @PathVariable("secondRunUid") int secondRunUid,
            @RequestBody RunsComparison runsComparison
            ) throws IOException {
        if(runService.hasBeenCreated(firstRunUid) && runService.hasBeenCreated(secondRunUid)){
            Run firstRun = runService.getRunByUid(firstRunUid);
            Run secondRun = runService.getRunByUid(secondRunUid);
            if(firstRun.getRunReport().getStatus()==RunStatusType.FINISHED
                    && secondRun.getRunReport().getStatus()==RunStatusType.FINISHED){
                onlineServiceInvoker.compare2Runs(firstRun, secondRun, runsComparison.getNotebookUri());
                return ResponseEntity.ok(Arrays.asList(new Run[] {firstRun, secondRun}));
            }else{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Run "+firstRunUid+ "and/or Run "+secondRunUid+" aren't/isn't finished !");}
        }else{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Run "+firstRunUid+ "and/or Run "+secondRunUid+" aren't/doesn't exist !");}
    }

    private ResponseEntity<RunReport> pauseRun(int runUid, RunReport currentRunReport) throws InterruptedException {
        if(currentRunReport.getStatus()==RunStatusType.STARTED || currentRunReport.getStatus()==RunStatusType.RUNNING){
            onlineServiceInvoker.INTERRUPT = true; //Set action variable
            Thread.sleep(5000); //wait for asynchronous method to finish reading/writing in the database
            currentRunReport.setStatus(RunStatusType.PAUSED);
            runReportService.setStatus(currentRunReport.getUid(), RunStatusType.PAUSED);
            return ResponseEntity.ok(currentRunReport);
        }else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Run with Uid = "+runUid+" isn't RUNNING now!");}
    }

    private ResponseEntity<RunReport> continueRun(int runUid, RunReport currentRunReport) throws Exception {
        if(currentRunReport.getStatus()==RunStatusType.PAUSED){
            onlineServiceInvoker.INTERRUPT = false; //Set action variable
            currentRunReport.setStatus(RunStatusType.RUNNING);
            runReportService.setStatus(currentRunReport.getUid(), RunStatusType.RUNNING);
            Run currentRun = runService.getRunByUid(runUid);
            JSONArray loanRequestsList = serializeToJSONArray(dataService
                    .getByDataSourceUid(currentRun.getDataSourceUid())
                    .getUri()); //set loan requests list for current run object
            currentRun.setRequestList(loanRequestsList);
            onlineServiceInvoker.getDecisionsFromDecisionService(currentRun);
            return ResponseEntity.ok(currentRunReport);
        }else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "run with Uid = "+runUid+" isn't paused now!");}
    }

    private ResponseEntity<RunReport> stopRun(int runUid, RunReport currentRunReport) throws InterruptedException {
        if(currentRunReport.getStatus()!= RunStatusType.FINISHED && currentRunReport.getStatus()!= RunStatusType.STOPPED){
            onlineServiceInvoker.INTERRUPT = true; //Set action variable
            if(currentRunReport.getStatus()!=RunStatusType.PAUSED) Thread.sleep(5000); //wait for asynchronous method to finish reading/writing in the database
            RunReport updatedRunReport = runReportService.getByRunReportUid(runUid);
            updatedRunReport.setStatus(RunStatusType.STOPPED);
            runReportService.setStatus(updatedRunReport.getUid(), RunStatusType.STOPPED);
            return ResponseEntity.ok(updatedRunReport);
        }else{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "run with Uid = "+runUid+" is already stopped/finished now!");}
    }

    private ResponseEntity<RunReport> getRunReport(int runUid, RunReport currentRunReport){
        if(currentRunReport.getStatus()!=RunStatusType.NOT_CREATED){ return ResponseEntity.ok(currentRunReport);
        }else{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation is not created!");}
    }

    /*@PostMapping("{simulationUid}/runs/{runUid}/continue")
    public ResponseEntity<String> continueRun(
            @PathVariable("simulationUid") int simulationUid,
            @PathVariable("runUid") int runUid) throws java.lang.Exception {
        if(simulationService.hasBeenCreated(simulationUid) &&
                runService.hasBeenCreated(runUid)
        ){
            RunReport currentRunReport = runReportService.getByRunUid(runUid);
            if(currentRunReport.getStatus()==RunStatusType.PAUSED){
                decisionServiceInvoker.INTERRUPT = false; //Set action variable
                currentRunReport.setStatus(RunStatusType.RUNNING);
                runReportService.setStatus(currentRunReport.getUid(), RunStatusType.RUNNING);
                Run currentRun = runService.getRunByUid(runUid);

                JSONArray loanRequestsList = serializeToJSONArray(dataService
                                                                    .getByDataSourceUid(currentRun.getDataSourceUid())
                                                                    .getUri()); //set loan requests list for current run object
                currentRun.setLoanRequestList(loanRequestsList);
                decisionServiceInvoker.getDecisionsFromDecisionService(currentRun);
                return ResponseEntity.ok("Run with uid = "+runUid+" is running now!");
            }else {return ResponseEntity.badRequest()
                        .body("run with Uid = "+runUid+" isn't paused now!");}
        }else{
            return ResponseEntity
                    .badRequest()
                    .body("Simulation uid or run uid doesn't exist!");
        }
    }

    @PostMapping("{simulationUid}/runs/{runUid}/stop")
    public ResponseEntity<String> stopRun(
            @PathVariable("simulationUid") int simulationUid,
            @PathVariable("runUid") int runUid) throws InterruptedException {
        if(simulationService.hasBeenCreated(simulationUid) && runService.hasBeenCreated(runUid)){
            RunReport currentRunReport = runReportService.getByRunUid(runUid);
            if(currentRunReport.getStatus()!= RunStatusType.FINISHED && currentRunReport.getStatus()!= RunStatusType.STOPPED){
                decisionServiceInvoker.INTERRUPT = true; //Set action variable
                Thread.sleep(1000); //wait 3 seconds for async method to finish reading/writing in the database
                currentRunReport.setStatus(RunStatusType.STOPPED);
                runReportService.setStatus(currentRunReport.getUid(), RunStatusType.STOPPED);
                return ResponseEntity.ok("run with Uid = "+runUid+" is finished/stopped now!");
            }else{return ResponseEntity.badRequest()
                            .body("run with Uid = "+runUid+" is stopped/finished now!");}
        }else{return ResponseEntity.badRequest()
                        .body("Simulation uid or run uid doesn't exist!");}
    }

    @GetMapping("{simulationUid}/runs/{runUid}/report")
    public ResponseEntity<RunReport> getRunReportAll(
            @PathVariable("simulationUid") int simulationUid,
            @PathVariable("runUid") int runUid){
        if(simulationService.hasBeenCreated(simulationUid) && runService.hasBeenCreated(runUid)){
            RunReport currentRunReport = runReportService.getByRunUid(runUid);
            if(currentRunReport.getStatus()!=RunStatusType.NOT_CREATED){ return ResponseEntity.ok(currentRunReport);
            }else{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation is not created!");}
        }else{ throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Simulation uid or run uid doesn't exist!"); }
    }
     */

    private Run convertDtoToEntity(RunDTO runDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(runDto, Run.class);
    }
}
