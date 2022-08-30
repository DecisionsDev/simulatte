# Connect SimuLatte to IBM Automation Decision Service (ADS)

Configure SimuLatte to work with your ADS instance.
Access your ADS installation or ask for an evaluation at this url ToDo
How to access samples (v1 and v2) projects - ToDo

### Configure ADS remote invocation
* How to get the endpoint of your deployed decision service?
   * Relative path example: _xxx/pf2/loan_approval/loanApprovalDecisionService/1.4.0/loanApprovalDecisionService-1.4.0.jar 
   * Execute
   * Extended execute
* How to manage security?
   * BasicAuth
   * Zen Token
* How to test?

No ADS jars needed in this case. Invocations go over REST HTTPS calls on the secure endoint.
No need of a Decision Service archive download in this case.

### Configure embedded ADS invocation
#### Retrieve ADS execution jars
For local decision execution in the SimuLatte Java VM the following jars are expected in the build of the micro service:
   * execution-api.jar
   * engine-de.jar

Download these jars from an ADS installation by accessing to the download service : 
```bash
<DECISION_DESIGNER_SERVER_URL>/download/
```

You can also use cURL commands to download these jars : 
```bash
curl -k -s -H "Authorization: ZenApiKey $(printf "<ZEN_USERNAME>:<ZEN_APIKEY>" | base64)" <DECISION_DESIGNER_SERVER_URL>/download/execution-api_<EXECUTION_API_VERSION>.jar -o execution-api_<EXECUTION_API_VERSION>.jar
curl -k -s -H "Authorization: ZenApiKey $(printf "<ZEN_USERNAME>:<ZEN_APIKEY>" | base64)" <DECISION_DESIGNER_SERVER_URL>/download/engine-de-api_<ENGINE_API_VERSION>.jar -o engine-de-api_<ENGINE_API_VERSION>.jar
```

Put the downloaded jars in the libs folder under the project base directory.

#### Retrieve the Decision Service archive
You specify the url in a simulation descriptor to get dynamically the compiled decision service archive (DSA).
In the online mode this jar is dynamically read when running the simulation. In the offline mode this jar is injected in a SimuLatte simulation uber jar.

We need:
* deploymentSpaceId
* decisionId

To retrieve the decision archive, go to the link below and fill in the `deploymentSpaceId` and `decisionId` fields: 
```bash
<DECISION_DESIGNER_SERVER_URL>/runtime/api/swagger-ui/#/Decision%20storage%20management/getArchive
```

Once the DSA downloaded, you can specify the uri in a simulation descriptor to make decisions from. 
