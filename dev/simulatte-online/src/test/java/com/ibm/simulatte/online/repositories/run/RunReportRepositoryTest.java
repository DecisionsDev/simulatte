package com.ibm.simulatte.online.repositories.run;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import com.ibm.simulatte.core.repositories.run.RunReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RunReportRepositoryTest {

    @Autowired
    private RunReportRepository runReportRepositoryTest;
    ObjectMapper mapper;

    private final String RUN_REPORT_MOCK = "{\n" +
            "     \"status\": \"STARTED\",\n" +
            "     \"numberOfDecisions\": 50,\n" +
            "     \"numberOfRequests\": 50,\n" +
            "     \"progress\": 1.0,\n" +
            "     \"duration\": 564,\n" +
            "     \"numberOfDecisionsPerSecond\": 88.65248\n" +
            "} ";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
        runReportRepositoryTest.deleteAll();
    }

    @Test
    @Disabled
    void findByUid() {
    }

    @Test
    @Disabled
    void updateStatus() throws JsonProcessingException {
        //given
        RunReport givenRunReport = runReportRepositoryTest.save(mapper.readValue(RUN_REPORT_MOCK, RunReport.class));
        System.out.println("TEST avant  : "+givenRunReport);
        //when
        runReportRepositoryTest.updateStatus(givenRunReport.getUid(), RunStatusType.FINISHED);

        //then
        System.out.println("TEST apres : "+runReportRepositoryTest.findByUid(givenRunReport.getUid()));
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getStatus()).isEqualTo(RunStatusType.FINISHED);
    }

    @Test
    @Disabled
    void updateDuration() throws JsonProcessingException {
        //given
        RunReport givenRunReport = runReportRepositoryTest.save(mapper.readValue(RUN_REPORT_MOCK, RunReport.class));

        //when
        runReportRepositoryTest.updateDuration(givenRunReport.getUid(), 100);

        //then
        int expectedDuration = 100;
        System.out.println("TEST : "+runReportRepositoryTest.findByUid(givenRunReport.getUid()));
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getDuration()).isEqualTo(expectedDuration);
    }

    @Test
    @Disabled
    void updateNumberOfDecisionsAndProgress() throws JsonProcessingException {
        //given
        RunReport givenRunReport = runReportRepositoryTest.save(mapper.readValue(RUN_REPORT_MOCK, RunReport.class));

        //when
        runReportRepositoryTest.updateNumberOfDecisionsAndProgress(givenRunReport.getUid(), 100, 0.5F);

        //then
        int expectedNumberOfDecisions = 100;
        float expectedProgress = 0.5F;
        System.out.println("TEST : "+runReportRepositoryTest.findByUid(givenRunReport.getUid()));
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getNumberOfDecisions()).isEqualTo(expectedNumberOfDecisions);
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getProgress()).isEqualTo(expectedProgress);
    }

    @Test
    @Disabled
    void updateStatusAndNumberOfDecisionsAndProgress() throws JsonProcessingException {
        //given
        RunReport givenRunReport = runReportRepositoryTest.save(mapper.readValue(RUN_REPORT_MOCK, RunReport.class));

        //when
        runReportRepositoryTest.updateStatusAndNumberOfDecisionsAndProgress(givenRunReport.getUid(), RunStatusType.FINISHED,100, 0.5F);

        //then
        int expectedNumberOfDecisions = 100;
        float expectedProgress = 0.5F;
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getNumberOfDecisions()).isEqualTo(expectedNumberOfDecisions);
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getProgress()).isEqualTo(expectedProgress);
        assertThat(runReportRepositoryTest.findByUid(givenRunReport.getUid()).getStatus()).isEqualTo(RunStatusType.FINISHED);
    }
}