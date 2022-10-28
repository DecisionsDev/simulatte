package com.ibm.simulatte.core.executions.offline;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.decision.run.JSONDecisionRunner;
import com.ibm.decision.run.provider.DecisionRunnerProvider;
import com.ibm.decision.run.provider.URLDecisionRunnerProvider;
import com.ibm.simulatte.core.datamodels.data.FileType;
import com.ibm.simulatte.core.datamodels.decisionService.executor.Mode;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import com.ibm.simulatte.core.executions.common.Invoker;
import com.ibm.simulatte.core.utils.DataManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static com.ibm.simulatte.core.datamodels.decisionService.executor.Type.JSE;
import static com.ibm.simulatte.core.datamodels.decisionService.executor.Type.SPARK_STANDALONE;
import static com.ibm.simulatte.core.utils.DataManager.serializeToJSONArray;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OfflineServiceInvoker extends Invoker implements Serializable {

    //public URLDecisionRunnerProvider provider = null;
    //public JSONDecisionRunner runner = null;

    @Autowired
    transient JavaSparkContext javaSparkContext;
    private boolean setRunAnalytics(RunReport runReport, int numberOfRequest){
        runReport.setNumberOfDecisions(numberOfRequest);
        runReport.setProgress((float) (numberOfRequest)/ runReport.getNumberOfRequests());
        return false;
    }
    ///////////////// MANAGE SIMULATION RUN OBJECT ///////////////////
    //@Async("customAsyncExecutor")
    public Run getDecisionsFromDecisionService(Run run) throws Exception {
        Function<String, String> executeDecisionService = request -> {
            URLDecisionRunnerProvider provider = null;
            JSONDecisionRunner runner = null;
            if (provider == null) {
                provider = new URLDecisionRunnerProvider
                                .Builder()
                                .addURL(new URL(String.format("file:///%s", run.getDecisionService().getEndPoint())))
                                .build();
            }

            if (runner == null) {
                runner = provider.getDecisionRunner(run.getDecisionService().getOperationName(), JSONDecisionRunner.class);
            }

            if (run.getOptimization()) {  // for optimization
                JSONObject jsonRequest = new JSONObject(request);
                for(Parameter parameter : run.getOptimizationParameters()){
                    jsonRequest = updateRequestParams(jsonRequest, parameter.getName(), parameter.getValue(), parameter.getType());
                }
                request = jsonRequest.toString();
            }

            Object in = runner.getInputReader().readValue(request);
            Object out = runner.execute(in);
            String responseBody = runner.getOutputWriter().writeValueAsString(out);
            JSONObject decisionData = decisionDataFormatter(run, request, responseBody);
            return decisionData.toString();
        };
        
        //RDD 
        JavaRDD<String> responsesFromDecisionService = null;

        // Read data in file system
        JSONArray requestsList = run.getExecutor().getType()==JSE
                ? serializeToJSONArray(run.getDataSource().getUri())
                : new JSONArray(javaSparkContext.textFile(run.getDataSource().getUri()).collect()); //convert to json array
        run.setRequestList(requestsList); //set request list for execution

        //Set run report
        System.out.println("RUN REPORT : "+new RunReport(RunStatusType.CREATED));
        RunReport currentRunReport = new RunReport(RunStatusType.CREATED);
        run.setRunReport(currentRunReport);
        if(run.getRunReport().getStatus()==RunStatusType.CREATED) {
            currentRunReport.setStatus(RunStatusType.STARTED);
            currentRunReport.setNumberOfDecisions(0);
            currentRunReport.setNumberOfRequests(run.getRequestList().length());
        }

        //Timer setup
        long start = System.currentTimeMillis();
        long timeElapsed = currentRunReport.getDuration();

        //Get decision from Decision Service for each loan request
        int counter = run.getRunReport().getNumberOfDecisions();

        Map<String, String> headerOptions = getHeaderOptions(run.getDecisionService()); //set decision service auth in request header config

        log.info("Your simulation  is running now !");

        if(run.getExecutor().getType()==JSE){
            JSONArray loanRequestsList = serializeToJSONArray(run.getDataSource().getUri()); //convert to json array
            run.setRequestList(loanRequestsList); //set requestList
            if (run.getExecutor().getMode()== Mode.LOCAL){
                DecisionRunnerProvider provider = new URLDecisionRunnerProvider
                                                        .Builder()
                                                        .addURL(new URL(String.format("file:///%s", run.getDecisionService().getEndPoint())))
                                                        .build();
                JSONDecisionRunner runner = provider.getDecisionRunner(run.getDecisionService().getOperationName(), JSONDecisionRunner.class);
                while(counter < run.getRequestList().length()){
                    JSONObject jsonRequest = run.getRequestList().getJSONObject(counter);
                    if(run.getOptimization()) {
                        for(Parameter parameter : run.getOptimizationParameters()){
                            jsonRequest = updateRequestParams(jsonRequest, parameter.getName(), parameter.getValue(), parameter.getType());
                        }
                    }
                    Object in = runner.getInputReader().readValue(jsonRequest.toString());
                    Object out = runner.execute(in);
                    String responseBody = runner.getOutputWriter().writeValueAsString(out);
                    JSONObject decisionData = decisionDataFormatter(run, jsonRequest.toString(), responseBody);
                    run.getDecisions().put(decisionData); //Store in current run object

                    if(setRunAnalytics(currentRunReport, counter+1)){
                        break;
                    }; //update run analytics from database
                    counter += 1;
                }
            }
        }
        if(run.getExecutor().getType()==SPARK_STANDALONE){
            JavaRDD<String> requestsToDecisionService = javaSparkContext.textFile(run.getDataSource().getUri());
            if (run.getExecutor().getMode()== Mode.LOCAL){
                responsesFromDecisionService = requestsToDecisionService.map(executeDecisionService);
            }
            run.setDecisions(new JSONArray(responsesFromDecisionService.collect()));
        }

        if(currentRunReport.getStatus()==RunStatusType.STARTED ||
                currentRunReport.getStatus()== RunStatusType.RUNNING){
            //store run status, numberDecisions, progress
            currentRunReport.setStatus(RunStatusType.FINISHED);
            currentRunReport.setNumberOfDecisions(run.getRequestList().length());
            log.info("Your simulation is finished !");
        }


        //set duration in database
        timeElapsed += (System.currentTimeMillis() - start);
        currentRunReport.setDuration((int)timeElapsed);

        //find matched data sink
        run.getDataSink().setUri(
                (run.getDataSink().getUri()!=null && run.getDataSink().getUri().length()>=10)
                    ? run.getDataSink().getUri()
                    : run.getDataSink().getFolderPath()+"/"+run.getDecisionService().getType().toString().toLowerCase()
                        +"-22.0.1-"+ run.getName()+"-decisions-"
                        +(run.getTrace() ? "with" : "no")
                        +"trace-"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+"-"
                        +(
                                run.getDecisions().length()>999_999
                                    ? String.valueOf(run.getDecisions().length()).substring(0, String.valueOf(run.getDecisions().length()).length() - 6) + "M"
                                    : run.getDecisions().length()>999
                                    ? String.valueOf(run.getDecisions().length()).substring(0, String.valueOf(run.getDecisions().length()).length() - 3) + "K"
                                    : run.getDecisions().length()
                        )
                        + (
                                run.getDataSink().getFormat().name()!=FileType.PARQUET.name()
                                        ? "."+run.getDataSink().getFormat().name().toLowerCase()
                                        : "")
                        );

        DataManager.writeInDataSink(
                run.getOptimization() || !run.getOptimizationParameters().isEmpty(),
                run.getDataSink().getUri(),
                run.getDecisions(),
                responsesFromDecisionService,
                run.getDataSink().getFormat()); //Write in data sink uri (file on user file system)

        return run;
    }

    public void compare2Runs(Run firstRun, Run secondRun, String notebookUri) throws IOException {
        Map<String, String> headerOptions = getHeaderOptions(firstRun.getDecisionService());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String firstJson = ow.writeValueAsString(firstRun);
        String secondJson = ow.writeValueAsString(secondRun);

        JSONObject payload = new JSONObject();
        payload.put("firstRun", firstJson);
        payload.put("secondRun", secondJson);
        payload.put("notebookUri", notebookUri);
        sendRequest("http://"+System.getenv("SIMULATTE_ANALYTIC_HOSTNAME")+":8000/simulation/compare-2-runs",
                        payload.toString(),
                        headerOptions);
    }
}
