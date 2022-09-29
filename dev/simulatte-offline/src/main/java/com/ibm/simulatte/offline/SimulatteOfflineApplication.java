package com.ibm.simulatte.offline;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.simulatte.core.datamodels.decisionService.executor.Executor;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.dto.RunDTO;
import com.ibm.simulatte.core.dto.SimulationDTO;
import com.ibm.simulatte.core.execution.offline.DecisionServiceInvoker;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.ibm.simulatte.core.configs", "com.ibm.simulatte.core.datamodels", "com.ibm.simulatte.core.execution.offline", "com.ibm.simulatte.core.execution.common", "com.ibm.simulatte.offline"})
public class SimulatteOfflineApplication implements CommandLineRunner {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    //@Value("${simulation.spec}")
    private String newSimulationSpec = "";

    public static void main(String[] args) {
       SpringApplication.run(SimulatteOfflineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(newSimulationSpec==""){
            log.warn("Using default simulation specification");
            newSimulationSpec = "{\"uid\":968,\"name\":\"mysimu\",\"description\":\"string\",\"createDate\":\"2022-09-21T13:01:40.928+00:00\",\"lastUpdateDate\":\"2022-09-21T13:01:40.928+00:00\",\"trace\":true,\"dataSource\":{\"format\":\"JSON\",\"uri\":\"/Users/tiemokodembele/Documents/internShip/simulatte-public/data/ADS/fraudDetection/ads-22.0.1-frauddetection-requests-20220921_130128-10.json\",\"username\":\"string\",\"password\":\"string\",\"uid\":3856},\"dataSink\":{\"format\":\"JSON\",\"folderPath\":\"/Users/tiemokodembele/Documents/internShip/simulatte-public/data/ADS/fraudDetection\",\"uri\":null,\"username\":\"string\",\"password\":\"string\",\"uid\":3855},\"decisionService\":{\"type\":\"ADS\",\"endPoint\":\"/Users/tiemokodembele/Documents/internShip/simulatte-public/libs/ADS/DSA/fraud_detection_1.9.0.jar\",\"authType\":\"BASIC_AUTH\",\"operationName\":\"detect-fraud-in-card-transactions\",\"username\":\"drsManager\",\"password\":\"manager\",\"key\":\"string\",\"value\":\"string\",\"token\":\"MDgyMjEzNzA2OjB6M3lwc29MZTJOOE1sQ2pDVDVSVXlmSHNXRDhDbjBKc1ZKZlN1aEI=\",\"headerPrefix\":\"string\",\"uid\":2888},\"metrics\":[{\"type\":\"SPARK_SQL\",\"uid\":968,\"name\":\"string\",\"description\":\"string\",\"expression\":\"string\"}],\"kpi\":[{\"type\":\"SPARK_SQL\",\"uid\":968,\"name\":\"string\",\"description\":\"string\"}],\"executor\":{\"type\":\"SPARK_STANDALONE\",\"mode\":\"LOCAL\",\"capability\":\"ODM\",\"uid\":0},\"optimization\":false,\"optimizationParameters\":[{\"uid\":0,\"name\":\"actualFraud\",\"value\":\"false\",\"type\":\"BOOLEAN\",\"description\":\"string\"}]}" ;
        }

        JSONObject newSimulationObject = new JSONObject(newSimulationSpec);
        if(newSimulationObject.has("executor")){
            Executor executor = objectMapper.readValue(newSimulationObject.remove("executor").toString(), Executor.class);
            Boolean optimization = Boolean.parseBoolean(newSimulationObject.remove("optimization").toString()) || false;
            JSONArray optimizationParameters = new JSONArray(newSimulationObject.remove("optimizationParameters").toString());

            SimulationDTO newSimulationDTO = objectMapper.readValue(newSimulationObject.toString(), SimulationDTO.class);
            Simulation newSimulation = convertDtoToEntity(newSimulationDTO);

            Run newRun = convertSimulationToRun(newSimulationDTO);
            newRun.setExecutor(executor);
            newRun.setOptimization(optimization);
            List<Parameter> optiParamsList = new ArrayList<Parameter>();
            for(int i = 0; i<optimizationParameters.length(); i++){
                String removedItem = optimizationParameters.remove(i).toString();
                Parameter parameter = objectMapper.readValue(removedItem, Parameter.class);
                optiParamsList.add(parameter);
            }
            newRun.setOptimizationParameters(optiParamsList);
            System.out.println("RUN OBJECT : "+newRun);

            DecisionServiceInvoker decisionServiceInvoker = new DecisionServiceInvoker();
            decisionServiceInvoker.getDecisionsFromDecisionService(newRun);
            System.out.println("DATA SINK URI : "+newRun.getDataSink().getUri());
        }else{
            SimulationDTO newSimulationDTO = objectMapper.readValue(newSimulationObject.toString(), SimulationDTO.class);
            Simulation newSimulation = convertDtoToEntity(newSimulationDTO);
            System.out.println("SIMU : "+newSimulation.toString());
            //new ShutdownManager().initiateShutdown("simulation doesn't have any 'executor' key.");
        }

        /*if(true){
            new ShutdownManager().initiateShutdown("simulation doesn't have any 'executor' key.");
        }
        JSONObject newRunObject = new JSONObject(newRunSpec);
        RunDTO newRunDTO = objectMapper.readValue(newRunObject.toString(), RunDTO.class);
        Run newRun = convertDtoToEntity(newRunDTO);*/
    }

    private Simulation convertDtoToEntity(SimulationDTO simulationDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(simulationDto, Simulation.class);
    }

    private Run convertDtoToEntity(RunDTO runDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(runDto, Run.class);
    }

    private Run convertSimulationToRun(SimulationDTO simulationDTO)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(simulationDTO, Run.class);
    }
}
