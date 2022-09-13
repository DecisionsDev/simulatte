package com.ibm.simulatte.core.dto;

import com.ibm.simulatte.core.datamodels.data.DataSink;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.ibm.simulatte.core.datamodels.decisionService.executor.Executor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RunDTO {

    @JsonProperty("uid")
    private int uid;

    @JsonProperty("simulationUid")
    private int simulationUid;

    @JsonProperty("name")
    private String name ;

    @JsonProperty("description")
    private String description ;

    @JsonProperty("createDate")
    private Date createDate;

    @JsonProperty("trace")
    private Boolean trace = null ; //Optional (if user want to change)

    /*
    @NonNull
    @JsonProperty("dataSource")
    private DataSource dataSource ; //Optional (if user want to change Data source type or/and uri)
     */

    @JsonProperty("optimization")
    private Boolean optimization = false ;


    @JsonProperty("dataSink")
    private DataSink dataSink = null ; //Optional (if user want to change Data sink folder or/and file type)

    @JsonProperty("decisionService")
    private DecisionService decisionService = null ; //Optional (if user want to change Decision Service type or/and endPoint)

    @JsonProperty("executor")
    private Executor executor;

    @JsonProperty("notebookUri")
    private String notebookUri ;

    @JsonProperty("optimizationParameters")
    private Set<Parameter> optimizationParameters;

    /*
    @NonNull
    @JsonProperty("metrics")
    private List<Metric> metrics; // Select metrics from your simulation object metrics

    @NonNull
    @JsonProperty("kpi")
    private List<KPI> kpi; // Select KPIs from your simulation object KPIs
     */
}
