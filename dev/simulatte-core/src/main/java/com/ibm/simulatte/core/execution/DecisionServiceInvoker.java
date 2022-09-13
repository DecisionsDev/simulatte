package com.ibm.simulatte.core.execution;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.decision.run.JSONDecisionRunner;
import com.ibm.decision.run.provider.DecisionRunnerProvider;
import com.ibm.decision.run.provider.URLDecisionRunnerProvider;
import com.ibm.simulatte.core.configs.webClient.HttpConfig;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.ibm.simulatte.core.datamodels.decisionService.Type;
import com.ibm.simulatte.core.datamodels.decisionService.executor.Mode;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.run.RunReport;
import com.ibm.simulatte.core.datamodels.run.RunStatusType;
import com.ibm.simulatte.core.services.data.DataSourceService;
import com.ibm.simulatte.core.services.run.RunReportService;
import com.ibm.simulatte.core.utils.DataManager;
import com.ibm.simulatte.core.utils.DefaultValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ibm.simulatte.core.datamodels.decisionService.decisionService.executor.Type.JSE;
import static com.ibm.simulatte.core.datamodels.decisionService.decisionService.executor.Type.SPARK_STANDALONE;
import static com.ibm.simulatte.core.utils.DataManager.serializeToJSONArray;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DecisionServiceInvoker implements Serializable {

    public static boolean INTERRUPT = false; //Test for PAUSE/CONTINUE

    @Getter @Setter
    public String mode = "online";

    public URLDecisionRunnerProvider provider = null;
    public JSONDecisionRunner runner = null;
    @Autowired
    private transient WebClient webClient;

    @Autowired
    transient JavaSparkContext javaSparkContext;

    @Autowired
    transient DataSourceService dataSourceService;

    @Autowired
    private transient RunReportService runReportService;

    public DecisionServiceInvoker (String invokerMode){
        this.mode = invokerMode;
    }

    final private WebClient getWebClient() {
        if(this.webClient==null) {
            try {
                return new HttpConfig().webClient();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            //log.warn("WebClient is reused !");
            return this.webClient;
        }
    }

    public ResponseEntity<String> sendRequest(String url, String request, Map<String, String> headerOptions) throws IOException {
        System.out.println("REQUEST : "+request);
        ResponseEntity<String> response = getWebClient().post()
                                .uri(URI.create(url))
                                .headers(httpHeaders -> {
                                    for (Map.Entry<String, String> entry : headerOptions.entrySet()) {
                                        httpHeaders.set(entry.getKey(), entry.getValue());
                                    }
                                })
                                .bodyValue(request)
                                .retrieve()
                                .toEntity(String.class)
                                .block();
        // check response
        if (response.getStatusCode() != HttpStatus.OK) { log.error("Request from Decision Service failed. Error status : "+response.getStatusCode()+" Error message : "+response.getBody()); }
        return response;
    }

    public Mono<JSONObject> sendRequestFlux(String url, String request, Map<String, String> headerOptions) {
        return getWebClient().post()
                .uri(URI.create(url))
                .headers(httpHeaders -> {
                    for (Map.Entry<String, String> entry : headerOptions.entrySet()) {
                        httpHeaders.set(entry.getKey(), entry.getValue());
                    }
                })
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JSONObject.class);
    }

    private JSONObject updateRequestParams(JSONObject jsonRequest, String key, String value, String type) throws IOException {
        switch (type){
            case "JSON":
                try {
                    return jsonRequest.put(key, new JSONObject(value));
                } catch (JSONException e) { throw new JSONException("Json data not parsable."); }
            case "INTEGER":
                return jsonRequest.put(key, Integer.parseInt(value));
            case "NUMBER":
                return jsonRequest.put(key, Long.parseLong(value));
            case "BOOLEAN":
                System.out.println("New request update : BOOLEAN ");
                return jsonRequest.put(key, Boolean.parseBoolean(value));
            default:
                return jsonRequest.put(key, value);
        }
    }

    private ResponseEntity<String> sendRequestWithParams(Run run, String request, Map<String, String> headerOptions) throws IOException {
        JSONObject jsonRequest = new JSONObject(request);
        if (run.getOptimization()) {  // for optimization
            for(Parameter parameter : run.getOptimizationParameters()){
                jsonRequest = updateRequestParams(jsonRequest, parameter.getName(), parameter.getValue(), parameter.getType());
            }
        }
        return sendRequest((run.getDecisionService().getType()== Type.ADS && run.getTrace())
                        ? run.getDecisionService().getEndPoint().replace("/execute","/extendedExecute")
                        : run.getDecisionService().getEndPoint(),
                (run.getDecisionService().getType()== Type.ODM && run.getTrace()) //check if ODM TRACE is wanted
                        ? jsonRequest.put("__TraceFilter__", new JSONObject(DefaultValue.ODM_DEFAULT_TRACE_CONFIG)).toString()
                        : (run.getDecisionService().getType()== Type.ADS && run.getTrace())
                            ? new JSONObject(DefaultValue.ADS_DEFAULT_TRACE_CONFIG).put("input",jsonRequest).toString()
                            : jsonRequest.toString(),
                headerOptions);
    }

    private JSONObject decisionDataFormatter(Run run, String request, String responseBody) {
        JSONObject jsonResponseBody = new JSONObject(responseBody);
        JSONObject jsonRequest = new JSONObject(request);

        JSONObject decision = new JSONObject();
        decision.put("request", jsonRequest);
        decision.put("trace", (run.getDecisionService().getType()== Type.ODM && run.getTrace())
                ? jsonResponseBody.remove("__decisionTrace__")
                : run.getDecisionService().getType()==Type.ADS && run.getTrace()
                ? jsonResponseBody.has("executionTrace")
                    ? jsonResponseBody.remove("executionTrace")
                    : "Key 'executionTrace' not available in response object."
                : "empty") ;
        decision.put("response", run.getDecisionService().getType()==Type.ODM
                ? responseBody
                : run.getDecisionService().getType()==Type.ADS && run.getTrace()
                    ? jsonResponseBody.has("output")
                        ? new JSONObject(jsonResponseBody.remove("output").toString())
                            .put("__DecisionID__", UUID.randomUUID().toString())
                        : new JSONObject(jsonResponseBody.toString())
                            .put("__DecisionID__", UUID.randomUUID().toString())
                    : jsonResponseBody.put("__DecisionID__", UUID.randomUUID().toString()));

        return decision;
    }

    public Map<String, String> getHeaderOptions(DecisionService decisionService){
        Map<String, String> headerOptions = new HashMap<String, String>();
        switch (decisionService.getAuthType()){
            case NO_AUTH:
                log.warn("Decision service auth type : NO AUTH");
                break;
            case BASIC_AUTH:
                log.warn("Decision service auth type : BASIC AUTH");
                String plainCredentials = decisionService.getUsername() + ":" + decisionService.getPassword();
                String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
                headerOptions.put("Authorization", String.format("Basic %s",base64Credentials)); // Create authorization header
                break;
            case BEARER_TOKEN:
                log.warn("Decision service auth type : BEARER TOKEN");
                headerOptions.put("Authorization", String.format("Bearer %s",decisionService.getToken()));
                break;
            case ZEN_TOKEN:
                log.warn("Decision service auth type : ZEN TOKEN");
                headerOptions.put("Authorization", String.format("ZenApiKey %s",decisionService.getToken()));
                break;
            case O_AUTH_2:
                log.error("Decision service auth type : O AUTH 2.0");
                headerOptions.put("Authorization", decisionService.getHeaderPrefix()+" "+decisionService.getToken());
                break;
            case API_KEY:
                log.warn("Decision service auth type : API KEY");
                headerOptions.put(decisionService.getKey(), decisionService.getValue());
                break;
            default:
                log.error("Decision service auth type : NONE ! Please check your payload.");
                break;
        }
        return headerOptions;
    }


    public boolean setRunAnalytics(int runUid, RunReport runReport, int numberOfRequest){
        runReport.setNumberOfDecisions(numberOfRequest);
        runReport.setProgress((float) (numberOfRequest)/ runReport.getNumberOfRequests());
        if(mode=="online"){
            runReportService.setNumberOfDecisionsAndProgress(runReport.getUid(), numberOfRequest,
                    (float) (numberOfRequest / runReport.getNumberOfRequests())); //Set run analytics

            runReport = runReportService.getByRunReportUid(runUid); //update run analytics from database

            if(INTERRUPT || runReport.getStatus()== RunStatusType.PAUSED ||
                    runReport.getStatus()== RunStatusType.STOPPED){
                log.info("Your simulation stop running !");
                return true;
            }
        }
        return false;
    }

    ///////////////// MANAGE SIMULATION RUN OBJECT ///////////////////
    //@Async("customAsyncExecutor")
    public void getDecisionsFromDecisionService(Run run) throws Exception {
        Function<String, String> executeDecisionService = new Function<String, String>() {

            public String call(String request) throws IOException {
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
            }
        };
        
        //RDD 
        JavaRDD<String> responsesFromDecisionService = null;

        // Read data in file system
        JSONArray requestsList = run.getExecutor().getType()==JSE
                ? serializeToJSONArray(mode=="online"
                        ? dataSourceService
                            .getByUid(run.getDataSourceUid())
                            .getUri()
                        : run.getDataSource().getUri())
                : new JSONArray(javaSparkContext.textFile(mode=="online"
                                                            ? dataSourceService
                                                                .getByUid(run.getDataSourceUid())
                                                                .getUri()
                                                            : run.getDataSource().getUri()).collect()); //convert to json array
        run.setRequestList(requestsList); //set request list for execution

        //Set run report
        RunReport currentRunReport = new RunReport();
        if(run.getRunReport().getStatus()==RunStatusType.CREATED) {
            if(mode=="online") currentRunReport = runReportService.getByRunReportUid(run.getUid());
            currentRunReport.setStatus(RunStatusType.STARTED);
            currentRunReport.setNumberOfDecisions(0);
            currentRunReport.setNumberOfRequests(run.getRequestList().length());
            if(mode=="online") runReportService.setByRunReportUid(currentRunReport);
        }

        //get current simulation run analytics
        if(mode=="online") currentRunReport = runReportService.getByRunReportUid(run.getUid());

        //Timer setup
        long start = System.currentTimeMillis();
        long timeElapsed = currentRunReport.getDuration();

        //Get decision from Decision Service for each loan request
        int counter = run.getRunReport().getNumberOfDecisions();

        Map<String, String> headerOptions = getHeaderOptions(run.getDecisionService()); //set decision service auth in request header config

        log.info("Your simulation  is running now !");

        if(run.getExecutor().getType()==JSE){
            JSONArray loanRequestsList = serializeToJSONArray(mode=="online"
                    ? dataSourceService.getByUid(run.getDataSourceUid()).getUri()
                    : run.getDataSource().getUri()); //convert to json array
            run.setRequestList(loanRequestsList); //set requestList

            if (run.getExecutor().getMode()== Mode.REMOTE){
                /*Flux.fromIterable(run.getLoanRequestList())
                        .parallel()
                        .map(request -> sendRequestFlux(run.getDecisionService().getEndPoint(), new JSONObject(request).toString(), headerOptions))
                        .subscribe(response -> System.out.println(Thread.currentThread().getName() + " from first list, got " + response.block().toString()));
                List<JSONObject> responses = Flux.fromIterable(run.getLoanRequestList())
                        .flatMap(request -> {
                            JSONObject jsonRequest = new JSONObject(request);
                            Mono<JSONObject> response = sendRequestFlux(
                                    (run.getDecisionService().getType()==Type.ADS && run.getTrace())
                                            ? run.getDecisionService().getEndPoint().replace("/execute","/extendedExecute")
                                            : run.getDecisionService().getEndPoint(),
                                    (run.getDecisionService().getType()== Type.ODM && run.getTrace()) //check if ODM TRACE is wanted
                                            ? jsonRequest.put("__TraceFilter__", new JSONObject(ODM_DEFAULT_TRACE_CONFIG)).toString()
                                            : (run.getDecisionService().getType()== Type.ADS && run.getTrace())
                                            ? new JSONObject(ADS_DEFAULT_TRACE_CONFIG).put("input",jsonRequest).toString()
                                            : jsonRequest.toString(),
                                    headerOptions);
                            //System.out.println("RESPONSE : "+response.block().toString());
                            return response;
                        })
                        .collectList()
                        .block();

                run.setDecisions(new JSONArray(responses));*/
                while(counter < run.getRequestList().length()){
                    String request = run.getRequestList().getJSONObject(counter).toString();
                    String responseBody = sendRequestWithParams(run, request, headerOptions).getBody();
                    JSONObject decisionData = decisionDataFormatter(run, request, responseBody);
                    run.getDecisions().put(decisionData); //Store in current run object

                    if(setRunAnalytics(run.getUid(), currentRunReport, counter+1)){
                        break;
                    }; //update run analytics from database

                    counter += 1;
                }
            }
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

                    if(setRunAnalytics(run.getUid(), currentRunReport, counter+1)){
                        break;
                    }; //update run analytics from database
                    counter += 1;
                }
            }
        }
        if(run.getExecutor().getType()==SPARK_STANDALONE){
            JavaRDD<String> requestsToDecisionService = javaSparkContext.textFile(mode=="online"
                    ? dataSourceService.getByUid(run.getDataSourceUid()).getUri()
                    : run.getDataSource().getUri());
            if (run.getExecutor().getMode()== Mode.REMOTE){
                responsesFromDecisionService = requestsToDecisionService.map((Function<String, String>) request -> {
                    String responseBody = sendRequestWithParams(run, request, headerOptions).getBody();
                    JSONObject decisionData = decisionDataFormatter(run, request, responseBody);
                    return decisionData.toString();
                });
            }
            if (run.getExecutor().getMode()== Mode.LOCAL){
                responsesFromDecisionService = requestsToDecisionService.map(executeDecisionService);
            }
            run.setDecisions(new JSONArray(responsesFromDecisionService.collect()));
        }

        if(!INTERRUPT && (currentRunReport.getStatus()==RunStatusType.STARTED ||
                currentRunReport.getStatus()== RunStatusType.RUNNING)){
            //store run status, numberDecisions, progress
            currentRunReport.setStatus(RunStatusType.FINISHED);
            currentRunReport.setNumberOfDecisions(run.getRequestList().length());
            if(mode=="online") runReportService.setByRunReportUid(currentRunReport);
            log.info("Your simulation is finished !");
        }


        //set duration in database
        timeElapsed += (System.currentTimeMillis() - start);
        currentRunReport.setDuration((int)timeElapsed);
        if(mode=="online") runReportService.setByRunReportUid(currentRunReport);

        //find matched data sink
        run.getDataSink().setUri(run
                                    .getDataSink().getUri()
                                    .replace("-0."+run.getDataSink().getFormat().name().toLowerCase(),
                                            String.format("-%s."+run.getDataSink().getFormat().name().toLowerCase(), run.getRunReport().getNumberOfDecisions()>999_999
                                                ? String.valueOf(run.getRunReport().getNumberOfDecisions()).substring(0, String.valueOf(run.getRunReport().getNumberOfDecisions()).length() - 6) + "M"
                                                : run.getRunReport().getNumberOfDecisions()>999
                                                    ? String.valueOf(run.getRunReport().getNumberOfDecisions()).substring(0, String.valueOf(run.getRunReport().getNumberOfDecisions()).length() - 3) + "K"
                                                    : run.getRunReport().getNumberOfDecisions() )));

        DataManager.writeInDataSink(run.getDataSink().getUri(), run.getDecisions(), responsesFromDecisionService, run.getDataSink().getFormat()); //Write in data sink uri (file on user file system)
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
        sendRequest("http://127.0.0.1:8000/simulation/compare-2-runs",
                        payload.toString(),
                        headerOptions);
    }
}
