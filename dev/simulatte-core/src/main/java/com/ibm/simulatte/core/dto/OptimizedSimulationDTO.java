package com.ibm.simulatte.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.simulatte.core.datamodels.analytic.KPI;
import com.ibm.simulatte.core.datamodels.analytic.Metric;
import com.ibm.simulatte.core.datamodels.data.DataSink;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.ibm.simulatte.core.datamodels.optimization.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class OptimizedSimulationDTO {

    @JsonProperty("uid")
    private int uid;

    @JsonProperty("name")
    private String name ;

    @JsonProperty("description")
    private String description ;

    @JsonProperty("createDate")
    private Date createDate ;

    @JsonProperty("lastUpdateDate")
    private Date lastUpdateDate;

    @JsonProperty("trace")
    private Boolean trace;

    @JsonProperty("dataSource")
    private DataSource dataSource ;

    @JsonProperty("dataSink")
    private DataSink dataSink ;

    @JsonProperty("decisionService")
    private DecisionService decisionService ;

    @JsonProperty("metrics")
    private List<Metric> metrics;

    @JsonProperty("kpi")
    private List<KPI> kpi ;

    @JsonProperty("optimizationParams")
    private List<Parameter> parameters;
}
