package com.ibm.simulatte.offline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.simulatte.core.datamodels.decisionService.executor.Executor;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.dto.RunDTO;
import com.ibm.simulatte.core.dto.SimulationDTO;
import com.ibm.simulatte.core.exception.NoSimulationDescriptionFoundException;
import com.ibm.simulatte.core.execution.offline.OfflineServiceInvoker;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class
        },
        scanBasePackages = {
                "com.ibm.simulatte.core.configs",
                "com.ibm.simulatte.core.datamodels",
                "com.ibm.simulatte.core.execution.offline",
                "com.ibm.simulatte.core.execution.common",
                "com.ibm.simulatte.offline"
        })
public class SimulatteOfflineApplication implements CommandLineRunner {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private OfflineServiceInvoker offlineServiceInvoker;

    public static void main(String[] args) {
       SpringApplication.run(SimulatteOfflineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JSONObject argsMap = new JSONObject();
        Stream.of(args).forEach(arg -> {
            if(arg.contains("=")) argsMap.put(arg.split("=")[0], arg.split("=")[1]);
        });
        String newSimulationSpecUri = argsMap.has("--simulation.spec.uri")
                                        ? argsMap.get("--simulation.spec.uri").toString()
                                        : argsMap.has("-ssu")
                                            ? argsMap.get("-ssu").toString()
                                            : null;
        System.out.println("URI SPEC : "+newSimulationSpecUri);

        if(newSimulationSpecUri==null) throw new NoSimulationDescriptionFoundException("Simulation spec not found.");

        String newSimulationSpec = new String(Files.readAllBytes(Paths.get(newSimulationSpecUri)), StandardCharsets.UTF_8);

        JSONObject newSimulationObject = new JSONObject(newSimulationSpec);
        if(newSimulationObject.has("executor")){
            Executor executor = objectMapper.readValue(newSimulationObject.remove("executor").toString(), Executor.class);
            Boolean optimization = Boolean.parseBoolean(newSimulationObject.remove("optimization").toString()) || false;
            JSONArray optimizationParameters = new JSONArray(newSimulationObject.remove("optimizationParameters").toString());

            SimulationDTO newSimulationDTO = objectMapper.readValue(newSimulationObject.toString(), SimulationDTO.class);

            Run newRun = convertSimulationToRun(newSimulationDTO);
            newRun.setExecutor(executor);
            newRun.setOptimization(optimization);
            List<Parameter> optimizationParamsList = new ArrayList<Parameter>();
            for(int i = 0; i<optimizationParameters.length(); i++){
                String removedItem = optimizationParameters.remove(i).toString();
                Parameter parameter = objectMapper.readValue(removedItem, Parameter.class);
                optimizationParamsList.add(parameter);
            }
            newRun.setOptimizationParameters(optimizationParamsList);

            Run runSpec = offlineServiceInvoker.getDecisionsFromDecisionService(newRun);
            JSONObject datasink = new JSONObject(newSimulationObject.get("dataSink").toString());
            datasink.put("uri", runSpec.getDataSink().getUri());
            System.out.println("DATA SINK : "+datasink.toString(4));
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
