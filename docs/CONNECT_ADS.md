# Connect SimuLatte to IBM Automation Decision Service (ADS)

Configure SimuLatte to work with your ADS instance.
Access your ADS installation or ask for an evaluation at this url ToDo
How to access samples (v1 and v2) projects - ToDo

### Configure ADS remote invocation
* How to get the endpoint of your deployed decision service?
   * Relative path example: _xxx/pf2/loan_approval/loanApprovalDecisionService/1.4.0/loanApprovalDecisionService-1.4.0.jar 
   * Extended execute
* How to manage security?
   * BasicAuth
   * Zen Token
* How to test?

No ADS jars needed in this case. Invocations go over REST HTTPS calls on the secure endoint.
No need of a Decision Service archive download in this case.

### Configure embedded ADS invocation
#### Retrieve ADS execution jars
For local decision execution in the SimuLatte Javav VM the following jars are expected in the build of the micro service:
   * execution-api.jar
   * engine-de.jar
   * jackson-databind version 2.13.2.2 and its dependencies
   * jackson-datatype-jsr310 version 2.13.2

Set the ADS_LIB_HOME variable where these jars have been made available.
You download these jars from an ADS installation by accessing to the download service.

#### Retrieve the Decision Service archive
You specify the url in a simulation descriptor to get dynamically the compiled decision service jar.
In the online mode this jar is dynamically read when running the simulation. In the offline mode this jar is injected in a SimuLatte simulation uber jar.

We need:
* deploymentSpaceId
* decisionId

To retrieve the decision archive, copy and paste this link in your web browser : 
   * https://your-cpd-cp4a/ads/runtime/api/swagger-ui/#/Decision%20storage%20management/getArchive
   * https://cpd-cp4a.apps.ads2201.cp.fyre.ibm.com/ads/runtime/api/swagger-ui/#/Decision%20storage%20management/getArchive
