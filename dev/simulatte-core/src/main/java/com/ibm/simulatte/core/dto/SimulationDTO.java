package com.ibm.simulatte.core.dto;

import com.ibm.simulatte.core.datamodels.analytic.KPI;
import com.ibm.simulatte.core.datamodels.analytic.Metric;
import com.ibm.simulatte.core.datamodels.data.DataSink;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationDTO {

    @JsonProperty("uid")
    private int uid;

    @JsonProperty("name")
    private String name ;

    @JsonProperty("description")
    private String description ;

    @JsonProperty("createDate")
    private Date createDate ;

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
}
