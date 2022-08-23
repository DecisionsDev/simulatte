package com.ibm.simulatte.online.simulation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.ibm.simulatte.core.repositories.simulation.SimulationRepository;
import com.ibm.simulatte.core.services.simulation.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private SimulationRepository simulationRepository;
    private SimulationService simulationServiceTest;

    @BeforeEach
    void setUp() {
        simulationServiceTest = new SimulationService(simulationRepository);
    }

    @Test
    void shouldCreateNewSimulation() throws JsonProcessingException {
        //given
        ObjectMapper mapper = new ObjectMapper();
        String SIMULATION_MOCK = "{\n" +
                "     \"name\": \"my simu\",\n" +
                "     \"description\": \"string\",\n" +
                "     \"createDate\": \"2022-05-18T23:31:19.828Z\",\n" +
                "     \"trace\": true,\n" +
                "     \"dataSource\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"uri\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation/odm-loanvalidation-requests-100.json\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"dataSink\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"folderPath\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"decisionService\": {\n" +
                "       \"type\": \"ODM\",\n" +
                "       \"endPoint\": \"http://localhost:9060/DecisionService/rest/miniloan/1.0/Miniloan_ServiceRuleset\",\n" +
                "       \"authType\": \"NO_AUTH\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\",\n" +
                "       \"key\": \"string\",\n" +
                "       \"value\": \"string\",\n" +
                "       \"token\": \"string\",\n" +
                "       \"headerPrefix\": \"string\"\n" +
                "     },\n" +
                "     \"metrics\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\",\n" +
                "         \"expression\": \"string\"\n" +
                "       }\n" +
                "     ],\n" +
                "     \"kpi\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\"\n" +
                "       }\n" +
                "     ]\n" +
                "   }";
        Simulation givenSimulation = mapper.readValue(SIMULATION_MOCK, Simulation.class);

        //When
        simulationServiceTest.create(givenSimulation);

        //then
        ArgumentCaptor<Simulation> simulationArgumentCaptor = ArgumentCaptor.forClass(Simulation.class);
        verify(simulationRepository).save(simulationArgumentCaptor.capture());
        Simulation capturedSimulation = simulationArgumentCaptor.getValue();
        assertThat(capturedSimulation).isEqualTo(givenSimulation);
    }

    @Test
    void willThrowWhenSimulationNameIsEmpty() throws JsonProcessingException {
        //given
        ObjectMapper mapper = new ObjectMapper();
        String SIMULATION_MOCK = "{\n" +
                "     \"name\": \"\",\n" +
                "     \"description\": \"string\",\n" +
                "     \"createDate\": \"2022-05-18T23:31:19.828Z\",\n" +
                "     \"trace\": true,\n" +
                "     \"dataSource\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"uri\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation/odm-loanvalidation-requests-100.json\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"dataSink\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"folderPath\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"decisionService\": {\n" +
                "       \"type\": \"ODM\",\n" +
                "       \"endPoint\": \"http://localhost:9060/DecisionService/rest/miniloan/1.0/Miniloan_ServiceRuleset\",\n" +
                "       \"authType\": \"NO_AUTH\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\",\n" +
                "       \"key\": \"string\",\n" +
                "       \"value\": \"string\",\n" +
                "       \"token\": \"string\",\n" +
                "       \"headerPrefix\": \"string\"\n" +
                "     },\n" +
                "     \"metrics\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\",\n" +
                "         \"expression\": \"string\"\n" +
                "       }\n" +
                "     ],\n" +
                "     \"kpi\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\"\n" +
                "       }\n" +
                "     ]\n" +
                "   }";
        Simulation givenSimulation = mapper.readValue(SIMULATION_MOCK, Simulation.class);

        //When

        //then
        assertThatThrownBy(() -> simulationServiceTest.create(givenSimulation))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Simulation name is empty.");
    }

    @Test
    void willThrowWhenDecisionServiceIsNull() throws JsonProcessingException {
        //given
        ObjectMapper mapper = new ObjectMapper();
        String SIMULATION_MOCK = "{\n" +
                "     \"name\": \"my simu\",\n" +
                "     \"description\": \"string\",\n" +
                "     \"createDate\": \"2022-05-18T23:31:19.828Z\",\n" +
                "     \"trace\": true,\n" +
                "     \"dataSource\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"uri\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation/odm-loanvalidation-requests-100.json\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"dataSink\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"folderPath\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"decisionService\": {},\n" +
                "     \"metrics\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\",\n" +
                "         \"expression\": \"string\"\n" +
                "       }\n" +
                "     ],\n" +
                "     \"kpi\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\"\n" +
                "       }\n" +
                "     ]\n" +
                "   }";
        Simulation givenSimulation = mapper.readValue(SIMULATION_MOCK, Simulation.class);

        //When

        //then
        assertThatThrownBy(() -> simulationServiceTest.create(givenSimulation))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Please, check your decision service type.");
    }

    @Test
    void shouldUpdateSimulation() throws JsonProcessingException {
        //given
        ObjectMapper mapper = new ObjectMapper();
        String SIMULATION_MOCK = "{\n" +
                "     \"name\": \"my simu\",\n" +
                "     \"description\": \"string\",\n" +
                "     \"createDate\": \"2022-05-18T23:31:19.828Z\",\n" +
                "     \"trace\": true,\n" +
                "     \"dataSource\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"uri\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation/odm-loanvalidation-requests-100.json\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"dataSink\": {\n" +
                "       \"format\": \"JSON\",\n" +
                "       \"folderPath\": \"/Users/tiemokodembele/Documents/internShip/simulatte/data/ODM/loanvalidation\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\"\n" +
                "     },\n" +
                "     \"decisionService\": {\n" +
                "       \"type\": \"ODM\",\n" +
                "       \"endPoint\": \"http://localhost:9060/DecisionService/rest/miniloan/1.0/Miniloan_ServiceRuleset\",\n" +
                "       \"authType\": \"NO_AUTH\",\n" +
                "       \"username\": \"string\",\n" +
                "       \"password\": \"string\",\n" +
                "       \"key\": \"string\",\n" +
                "       \"value\": \"string\",\n" +
                "       \"token\": \"string\",\n" +
                "       \"headerPrefix\": \"string\"\n" +
                "     },\n" +
                "     \"metrics\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\",\n" +
                "         \"expression\": \"string\"\n" +
                "       }\n" +
                "     ],\n" +
                "     \"kpi\": [\n" +
                "       {\n" +
                "         \"uid\": 0,\n" +
                "         \"name\": \"string\",\n" +
                "         \"type\": \"SPARK_SQL\",\n" +
                "         \"description\": \"string\"\n" +
                "       }\n" +
                "     ]\n" +
                "   }";
        Simulation givenSimulation = mapper.readValue(SIMULATION_MOCK, Simulation.class);

        //When
        simulationServiceTest.update(givenSimulation);

        //then
        ArgumentCaptor<Simulation> simulationArgumentCaptor = ArgumentCaptor.forClass(Simulation.class);
        verify(simulationRepository).save(simulationArgumentCaptor.capture());
        Simulation capturedSimulation = simulationArgumentCaptor.getValue();
        assertThat(capturedSimulation).isEqualTo(givenSimulation);
    }

    @Test
    void hasBeenCreated() {
        //given
        int givenSimulationUid = 1;

        //When
        simulationServiceTest.hasBeenCreated(givenSimulationUid);

        //then
        ArgumentCaptor<Integer> simulationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(simulationRepository).existsByUid(simulationArgumentCaptor.capture());
        int capturedSimulationUid = simulationArgumentCaptor.getValue();
        assertThat(capturedSimulationUid).isEqualTo(givenSimulationUid);
    }

    @Test
    void findMatchedSimulation() {
        //given
        int givenSimulationUid = 1;

        //When
        simulationServiceTest.findMatchedSimulation(givenSimulationUid);

        //then
        ArgumentCaptor<Integer> simulationArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(simulationRepository).findByUid(simulationArgumentCaptor.capture());
        int capturedSimulationUid = simulationArgumentCaptor.getValue();
        assertThat(capturedSimulationUid).isEqualTo(givenSimulationUid);
    }

    @Test
    @Disabled
    void getAllSimulations() {
    }
}