package com.ibm.simulatte.core.utils;

public class DefaultValue {
    public static final String TRACE = "\"__TraceFilter__\": {\n" +
            "      \"infoRulesetProperties\": false,\n" +
            "      \"infoOutputString\": false,\n" +
            "      \"infoInputParameters\": false,\n" +
            "      \"infoOutputParameters\": true,\n" +
            "      \"none\": true,\n" +
            "      \"infoExecutionEventsAsked\": false,\n" +
            "      \"workingMemoryFilter\": \"string\",\n" +
            "      \"infoBoundObjectByRule\": false,\n" +
            "      \"infoExecutionDuration\": true,\n" +
            "      \"infoExecutionDate\": true,\n" +
            "      \"infoExecutionEvents\": true,\n" +
            "      \"infoInetAddress\": true,\n" +
            "      \"infoRules\": true,\n" +
            "      \"infoRulesNotFired\": true,\n" +
            "      \"infoSystemProperties\": false,\n" +
            "      \"infoTasks\": true,\n" +
            "      \"infoTasksNotExecuted\": true,\n" +
            "      \"infoTotalRulesFired\": true,\n" +
            "      \"infoTotalRulesNotFired\": true,\n" +
            "      \"infoTotalTasksExecuted\": true,\n" +
            "      \"infoTotalTasksNotExecuted\": true,\n" +
            "      \"infoWorkingMemory\": true,\n" +
            "      \"infoRulesFired\": true,\n" +
            "      \"infoTasksExecuted\": true,\n" +
            "      \"infoBoundObjectSerializationType\": \"ClassName\"\n" +
            "    }";

    public static final String ODM_DEFAULT_TRACE_CONFIG = "{\n" +
            "    \"infoRulesetProperties\": true,\n" +
            "    \"infoOutputString\": false,\n" +
            "    \"infoInputParameters\": false,\n" +
            "    \"infoOutputParameters\": true,\n" +
            "    \"none\": true,\n" +
            "    \"infoExecutionEventsAsked\": false,\n" +
            "    \"workingMemoryFilter\": \"string\",\n" +
            "    \"infoBoundObjectByRule\": true,\n" +
            "    \"infoExecutionDuration\": true,\n" +
            "    \"infoExecutionDate\": true,\n" +
            "    \"infoExecutionEvents\": true,\n" +
            "    \"infoInetAddress\": true,\n" +
            "    \"infoRules\": false,\n" +
            "    \"infoRulesNotFired\": true,\n" +
            "    \"infoSystemProperties\": true,\n" +
            "    \"infoTasks\": false,\n" +
            "    \"infoTasksNotExecuted\": false,\n" +
            "    \"infoTotalRulesFired\": false,\n" +
            "    \"infoTotalRulesNotFired\": false,\n" +
            "    \"infoTotalTasksExecuted\": false,\n" +
            "    \"infoTotalTasksNotExecuted\": true,\n" +
            "    \"infoWorkingMemory\": true,\n" +
            "    \"infoRulesFired\": true,\n" +
            "    \"infoTasksExecuted\": false,\n" +
            "    \"infoBoundObjectSerializationType\": \"ClassName\"\n" +
            "  }";

    public  static final String ADS_DEFAULT_TRACE_CONFIG = "{\n" +
            "  \"executionId\": \"string\",\n" +
            "  \"executionTraceFilters\": {\n" +
            "    \"executionDuration\": true,\n" +
            "    \"printedMessages\": true,\n" +
            "    \"decisionModel\": {\n" +
            "      \"inputParameters\": \"ClassNameNHashcode\",\n" +
            "      \"outputParameters\": \"ClassNameNHashcode\",\n" +
            "      \"inputNode\": \"ClassNameNHashcode\",\n" +
            "      \"outputNode\": \"ClassNameNHashcode\"\n" +
            "    },\n" +
            "    \"rules\": {\n" +
            "      \"boundObjectsAtStart\": \"ClassNameNHashcode\",\n" +
            "      \"boundObjectsAtEnd\": \"ClassNameNHashcode\",\n" +
            "      \"allRules\": true,\n" +
            "      \"executedRules\": true,\n" +
            "      \"nonExecutedRules\": true,\n" +
            "      \"exceptions\": \"None\"\n" +
            "    },\n" +
            "    \"ruleflow\": {\n" +
            "      \"inputParameters\": \"ClassNameNHashcode\",\n" +
            "      \"outputParameters\": \"ClassNameNHashcode\",\n" +
            "      \"allTasks\": true,\n" +
            "      \"executedTasks\": true,\n" +
            "      \"notExecutedTasks\": true,\n" +
            "      \"selectedRules\": true\n" +
            "    }\n" +
            "  },\n" +
            "  \"deploymentSpaceId\": \"string\"\n" +
            "}";

    public static final String DEFAULT_ADS_PAYLOAD = "{\n" +
            "    \"loan\": {\n" +
            "      \"amount\": 185000,\n" +
            "      \"numberOfMonthlyPayments\": 120,\n" +
            "      \"startDate\": \"2005-06-01T00:00:00Z\",\n" +
            "      \"loanToValue\": 0.7\n" +
            "    },\n" +
            "    \"borrower\": {\n" +
            "      \"SSN\": {\n" +
            "        \"areaNumber\": \"123\",\n" +
            "        \"groupCode\": \"45\",\n" +
            "        \"serialNumber\": \"6789\"\n" +
            "      },\n" +
            "      \"firstName\": \"John\",\n" +
            "      \"lastName\": \"Doe\",\n" +
            "      \"birthDate\": \"1968-05-12T00:00:00Z\",\n" +
            "      \"yearlyIncome\": 100000,\n" +
            "      \"zipCode\": \"91320\",\n" +
            "      \"creditScore\": 500\n" +
            "    }\n" +
            "}" ;

    public static final String DEFAULT_ODM_PAYLOAD = "{\"borrower\":{\"name\":\"franck\",\"creditScore\":730,\"yearlyIncome\":315000},\"loan\": {\"amount\": 300000,\"duration\": 240,\"yearlyInterestRate\": 0.03,\"yearlyRepayment\": 10000}}";
}
