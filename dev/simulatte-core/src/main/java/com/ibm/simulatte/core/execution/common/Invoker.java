package com.ibm.simulatte.core.execution.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ibm.simulatte.core.configs.webClient.HttpConfig;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.ibm.simulatte.core.datamodels.decisionService.Type;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.utils.DefaultValue;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class Invoker implements Serializable {

    //To implement
    public abstract Run getDecisionsFromDecisionService(Run run) throws Exception;
    public abstract void compare2Runs(Run firstRun, Run secondRun, String notebookUri) throws IOException;

    @Autowired
    private transient WebClient webClient;

    public WebClient getWebClient() {
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

    public JSONObject updateRequestParams(JSONObject jsonRequest, String key, String value, String type) throws IOException {
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
                return jsonRequest.put(key, Boolean.parseBoolean(value));
            default:
                return jsonRequest.put(key, value);
        }
    }

    public ResponseEntity<String> sendRequestWithParams(Run run, String request, Map<String, String> headerOptions) throws IOException {
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

    public JSONObject decisionDataFormatter(Run run, String request, String responseBody) {
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
                ? jsonResponseBody
                : run.getDecisionService().getType()==Type.ADS && run.getTrace()
                ? jsonResponseBody.has("output")
                ? new JSONObject(jsonResponseBody.remove("output").toString())
                .put("__DecisionID__", UUID.randomUUID().toString())
                : jsonResponseBody
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

}
