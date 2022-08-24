# Connect Simulatte to IBM Automation Decision Service (ADS)

Access your ADS installation or ask for an evaluation at this url ToDo
How to access samples (v1 and v2) projects - ToDo

### Configure remote invocation
How to get the endpoint of your deployed decision service?
How to manage security?
How to test?

### Configure embedded invocation
#### Retrieve ADS execution jars
You need to get the following jars to embedd an ADS decision service execution in Simulatte:
   * execution-api.jar
   * engine-de.jar
   * jackson-databind version 2.13.2.2 and its dependencies
   * jackson-datatype-jsr310 version 2.13.2

Set the ADS_LIB_HOME variable where these jars have been made available.
You download these jars from an ADS installation by doing this ...

Über jar files
Java API ADS
Documentation on Java API ADS

An application using the ADS execution Java API must add in the classpath:

To retrieve the execution-api.jar and engine-de.jar files: jar

ADS decision service archive
#### Decision Service archive
Requirements to download the decision archives :

deploymentSpaceId
decisionId
To retrieve the decision archive, copy and paste this link in your web browser : https://cpd-cp4a.apps.ads2201.cp.fyre.ibm.com/ads/runtime/api/swagger-ui/#/Decision%20storage%20management/getArchive
