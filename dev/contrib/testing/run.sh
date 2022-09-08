#!/bin/sh
curl -X 'POST' \
  'http://localhost:8080/v1/simulation/create' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
     "uid": 49,
     "userUid": 0,
     "name": "my simu",
     "description": "string",
     "createDate": "2022-09-05T09:25:08.274+00:00",
     "lastUpdateDate": "2022-09-05T09:25:08.274+00:00",
     "trace": true,
     "dataSource": {
          "format": "JSON",
          "uri": "../../data/ADS/frauddetection/ads-22.0.1-frauddetection-requests-20220819_171942-100.json",
          "username": "string",
          "password": "string",
          "uid": 307
     },
     "dataSink": {
          "format": "JSON",
          "folderPath": "../../data/ADS/fraudDetection",
          "uri": null,
          "username": "string",
          "password": "string",
          "uid": 306
     },
     "decisionService": {
          "type": "ADS",
          "endPoint": "https://cpd-cp4a.apps.ads2201.cp.fyre.ibm.com/ads/runtime/api/v1/deploymentSpaces/embedded/decisions/_082392706%2Fbanking%2Fapproval_with_tasks%2FloanApprovalWithTasksDecisionService%2F1.1.0%2FloanApprovalWithTasksDecisionService-1.1.0.jar/operations/approvalWithTasks/execute",
          "authType": "BASIC_AUTH",
          "operationName": null,
          "username": "drsManager",
          "password": "manager",
          "key": "string",
          "value": "string",
          "token": "MDgyMjEzNzA2OjB6M3lwc29MZTJOOE1sQ2pDVDVSVXlmSHNXRDhDbjBKc1ZKZlN1aEI=",
          "headerPrefix": "string",
          "uid": 258
     },
     "metrics": [
          {
               "type": "SPARK_SQL",
               "uid": 49,
               "name": "string",
               "description": "string",
               "expression": "string"
          }
     ],
     "kpi": [
          {
               "type": "SPARK_SQL",
               "uid": 49,
               "name": "string",
               "description": "string"
          }
     ],
     "simulationReport": null
} '
