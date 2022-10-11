package com.ibm.simulatte.core.services.run;

import com.ibm.simulatte.core.datamodels.data.Type;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import com.ibm.simulatte.core.execution.online.OnlineServiceInvoker;
import com.ibm.simulatte.core.repositories.run.RunRepository;
import com.ibm.simulatte.core.services.data.DataService;
import com.ibm.simulatte.core.services.simulation.SimulationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class RunService {
    @Autowired
    RunRepository runRepository;

    @Autowired
    SimulationService simulationService;

    @Autowired
    JavaSparkContext javaSparkContext;

    @Autowired
    private DataService dataService;

    @Autowired
    private OnlineServiceInvoker onlineServiceInvoker;

    public Run startToRun(Run newRun) throws Exception {
        //set run report
        newRun.setRunReport(new RunReport());
        newRun.getRunReport().setStatus(RunStatusType.CREATED);

        newRun.setDataSourceUid(dataService
                                    .getDataSource(newRun.getSimulationUid(),Type.DATA_SOURCE.name())
                                    .getUid()); //find matched data source

        //find matched data sink
        System.out.println("DATA SINK BY USER : "+newRun.getDataSink().getUri());
        newRun.getDataSink().setUri((newRun.getDataSink().getUri()!=null && newRun.getDataSink().getUri().length()>=10)
                ? newRun.getDataSink().getUri()
                : dataService
                        .getDataSink(newRun.getSimulationUid(), Type.DATA_SINK.name())
                        .getFolderPath()+"/"+newRun.getDecisionService().getType().toString().toLowerCase()+"-22.0.1-"+ newRun.getName()+"-decisions-"
                        +(newRun.getTrace() ? "with" : "no")
                        +"trace-"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+"-"+newRun.getDecisions().length()+"."+newRun.getDataSink().getFormat().name().toLowerCase());

        // Read data in file system
        /*JSONArray loanRequestsList = serializeToJSONArray(dataService
                .getByDataSourceUid(newRun.getDataSourceUid())
                .getUri()); //convert to json array

        JSONArray loanRequestsList = new JSONArray(javaSparkContext.textFile(dataService
                .getByDataSourceUid(newRun.getDataSourceUid())
                .getUri()).collect())*/
        //JSONArray loanRequestsList = new JSONArray();
        //newRun.setRequestList(loanRequestsList); //get last record in database
        runRepository.save(newRun); //save in database and set to CREATED
        newRun = runRepository.findTopByOrderByUidDesc(); //get last run recorded in database
        onlineServiceInvoker.getDecisionsFromDecisionService(newRun);

        System.out.println("####################################################################################");
        System.out.println("########## THESE UIDs ALLOW YOU TO OBTAIN SIMULATION AND RUN INFORMATION ###########");
        System.out.println("####################### SIMULATION UID = "+ newRun.getSimulationUid()+" and RUN UID = "+ newRun.getUid()+ " #########################");
        System.out.println("####################################################################################");

        return runRepository.findByUid(newRun.getUid()); //retrieve the current run information
    }

    public boolean hasBeenCreated(int runUid) {
        return runRepository.existsByUid(runUid);
    }

    public List<Run> getAllRunsBySimulationUid(int simulationUid) {
        return runRepository.findAllBySimulationUid(simulationUid);
    }
    public Run getRunByUid(int runUid){
        return runRepository.findByUid(runUid);
    }

    public Run getRunBySimulationUid(int simulationUid, int runUid) {
        return runRepository.findBySimulationUidAndUid(simulationUid, runUid);
    }

    public Run saveRun(Run newRun){
        return runRepository.save(newRun);
    }
}
