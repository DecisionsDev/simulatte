package com.ibm.simulatte.core.datamodels.run;

import com.ibm.simulatte.core.datamodels.data.DataSink;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.ibm.simulatte.core.datamodels.executor.Executor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.json.JSONArray;
import org.springframework.data.annotation.Id;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "run")
public class Run implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private int uid;

    @NonNull
    private int simulationUid;

    @NonNull
    private String name ;

    private String description ;

    @CreationTimestamp
    private Date createDate;

    @NonNull
    private Boolean trace; //Optional (if user want to change it)

    @NonNull
    @JsonIgnore
    private int dataSourceUid ;

    @Transient
    private DataSource dataSource;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private DataSink dataSink ; //Optional (if user want to change Data sink folder or/and file type)

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private DecisionService decisionService ; //Optional (if user want to change Decision Service type or/and endPoint)

    //@NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private Executor executor;

    /*
    @NonNull
    private List<Metric> metrics; // Select metrics from your simulation object metrics

    @NonNull
    private List<KPI> kpi; // Select KPIs from your simulation object KPIs
     */

    @OneToOne(cascade = CascadeType.ALL)
    private RunReport runReport;

    private String notebookUri ;

    @JsonIgnore
    @Transient
    private String requests;

    @JsonIgnore
    @Transient
    private transient JSONArray requestList;

    @JsonIgnore
    @Transient
    private String decisionsStorage;

    @JsonIgnore
    @Transient
    private transient JSONArray decisions = new JSONArray();

}
