package com.ibm.simulatte.offline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.dto.SimulationDTO;
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
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication(
        scanBasePackages = {
                "com.ibm.simulatte.offline",
                "com.ibm.simulatte.core"
        },
        exclude = {DataSourceAutoConfiguration.class})
@EntityScan(basePackages = {"com.ibm.simulatte.core.datamodels"})
public class SimulatteOfflineApplication implements CommandLineRunner {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;
    @Value("${simulation.spec}")
    private String newSimulationSpec;



    public static void main(String[] args) {
        SpringApplication.run(SimulatteOfflineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(args).forEach(System.out::println);
        JSONObject newSimulationObject = new JSONObject(newSimulationSpec);

        /*JSONObject newDataSourceObject = new JSONObject(newSimulationObject.getString("dataSource"));
        JSONObject newDataSink = new JSONObject(newSimulationObject.getString("dataSource"));
        JSONObject newDecisionServiceObject = new JSONObject(newSimulationObject.getString("dataSource"));
        JSONObject newMetricsObject = new JSONObject(newSimulationObject.getString("dataSource"));
        JSONObject newKPIsObject = new JSONObject(newSimulationObject.getString("dataSource"));*/
        SimulationDTO newSimulationDTO = objectMapper.readValue(newSimulationSpec, SimulationDTO.class);
        Simulation newSimulation = convertDtoToEntity(newSimulationDTO);
        /*Simulation newSimulation = new Simulation(
                0,
                newSimulationObject.getInt("userUid"),
                newSimulationObject.getString("name"),
                newSimulationObject.getString("description"),
                newSimulationObject.getString("createDate"),
                newSimulationObject.getString("lastUpdateDate"),
                newSimulationObject.getString("trace"),
                newSimulationObject.getString("dataSource"),
                newSimulationObject.getString("dataSink"),
                newSimulationObject.getString("decisionService"),
                newSimulationObject.getString("metrics"),
                newSimulationObject.getString("kpi")
        );*/
        System.out.println("SIMULATION : "+newSimulation.toString());

    }

    private Simulation convertDtoToEntity(SimulationDTO simulationDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(simulationDto, Simulation.class);
    }
}
