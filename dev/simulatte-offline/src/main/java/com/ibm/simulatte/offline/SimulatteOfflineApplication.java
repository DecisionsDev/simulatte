package com.ibm.simulatte.offline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.datamodels.executor.Executor;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.dto.RunDTO;
import com.ibm.simulatte.core.dto.SimulationDTO;
import com.ibm.simulatte.offline.configs.shutdown.ShutdownManager;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.ibm.simulatte.offline.configs.shutdown.ShutdownManager.initiateShutdown;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SimulatteOfflineApplication implements CommandLineRunner {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    //@Value("${simulation.spec}")
    private String newSimulationSpec = "";

    //@Value("${run.spec}")
    private String newRunSpec = "";

    public static void main(String[] args) {
       ApplicationContext appcontext = SpringApplication.run(SimulatteOfflineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(newSimulationSpec==""){
            newSimulationSpec = "{\"uid\":46,\"userUid\":0,\"name\":\"mysimu\",\"description\":\"string\",\"createDate\":\"2022-08-23T16:04:02.906+00:00\",\"lastUpdateDate\":\"2022-08-23T16:04:02.906+00:00\",\"trace\":true,\"dataSource\":{\"format\":\"JSON\",\"uri\":\"/Users/tiemokodembele/Documents/internShip/simulatte/data/ADS/fraudDetection/ads-22.0.1-frauddetection-requests-20220823_180323-10K\",\"username\":\"string\",\"password\":\"string\",\"uid\":295},\"dataSink\":{\"format\":\"JSON\",\"folderPath\":\"/Users/tiemokodembele/Documents/internShip/simulatte/data/ADS/fraudDetection\",\"uri\":null,\"username\":\"string\",\"password\":\"string\",\"uid\":294},\"decisionService\":{\"type\":\"ADS\",\"endPoint\":\"https://cpd-cp4a.apps.ads2201.cp.fyre.ibm.com/ads/runtime/api/v1/deploymentSpaces/embedded/decisions/_082392706%2Fbanking%2Fapproval_with_tasks%2FloanApprovalWithTasksDecisionService%2F1.1.0%2FloanApprovalWithTasksDecisionService-1.1.0.jar/operations/approvalWithTasks/execute\",\"authType\":\"BASIC_AUTH\",\"operationName\":null,\"username\":\"drsManager\",\"password\":\"manager\",\"key\":\"string\",\"value\":\"string\",\"token\":\"MDgyMjEzNzA2OjB6M3lwc29MZTJOOE1sQ2pDVDVSVXlmSHNXRDhDbjBKc1ZKZlN1aEI=\",\"headerPrefix\":\"string\",\"uid\":249},\"metrics\":[{\"type\":\"SPARK_SQL\",\"uid\":46,\"name\":\"string\",\"description\":\"string\",\"expression\":\"string\"}],\"kpi\":[{\"type\":\"SPARK_SQL\",\"uid\":46,\"name\":\"string\",\"description\":\"string\"}],\"simulationReport\":null}";
        }
        if(newRunSpec==""){
            newRunSpec = "{\"uid\":206,\"simulationUid\":46,\"name\":\"frauddetection\",\"description\":\"string\",\"createDate\":\"2022-08-23T16:06:10.127+00:00\",\"trace\":false,\"dataSink\":{\"format\":\"PARQUET\",\"folderPath\":\"/Users/tiemokodembele/Documents/internShip/simulatte/data/ADS/fraudDetection\",\"uri\":\"/Users/tiemokodembele/Documents/internShip/simulatte/data/ADS/fraudDetection/ads-22.0.1-frauddetection-decisions-notrace-20220823_180610-10K.parquet\",\"username\":\"string\",\"password\":\"string\",\"uid\":298},\"decisionService\":{\"type\":\"ADS\",\"endPoint\":\"/Users/tiemokodembele/Documents/internShip/simulatte/dev/simulatte-core/src/main/resources/lib/fraud_detection_1.2.0.jar\",\"authType\":\"BASIC_AUTH\",\"operationName\":\"detection\",\"username\":\"drsManager\",\"password\":\"manager\",\"key\":\"string\",\"value\":\"string\",\"token\":\"MDgyMjEzNzA2OjB6M3lwc29MZTJOOE1sQ2pDVDVSVXlmSHNXRDhDbjBKc1ZKZlN1aEI=\",\"headerPrefix\":\"string\",\"uid\":252},\"executor\":{\"type\":\"SPARK_STANDALONE\",\"mode\":\"LOCAL\",\"capability\":\"ODM\",\"uid\":206},\"runReport\":{\"uid\":206,\"status\":\"FINISHED\",\"numberOfDecisions\":10000,\"numberOfRequests\":10000,\"progress\":0.0,\"duration\":557,\"numberOfDecisionsPerSecond\":17953.322},\"notebookUri\":\"/Users/tiemokodembele/Documents/internShip/simulatte/notebook/ads-vs-odm-loanvalidation-analytics.ipynb\"}";
        }

        JSONObject newSimulationObject = new JSONObject(newSimulationSpec);
        /*Executor executor = newSimulationObject.has("executor")
                ? objectMapper.readValue(newSimulationObject.get("executor").toString(), Executor.class)
                : null;*/
        if(true){
            new ShutdownManager().initiateShutdown("simulation doesn't have any 'executor' key.");
        }

        SimulationDTO newSimulationDTO = objectMapper.readValue(newSimulationObject.remove("executor").toString(), SimulationDTO.class);
        Simulation newSimulation = convertDtoToEntity(newSimulationDTO);

        /*JSONObject newRunObject = new JSONObject(newRunSpec);
        RunDTO newRunDTO = objectMapper.readValue(newRunObject.toString(), RunDTO.class);
        Run newRun = convertDtoToEntity(newRunDTO);*/

        //System.out.println("EXECUTOR : "+executor.toString());
    }

    private Simulation convertDtoToEntity(SimulationDTO simulationDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(simulationDto, Simulation.class);
    }

    private Run convertDtoToEntity(RunDTO runDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(runDto, Run.class);
    }
}
