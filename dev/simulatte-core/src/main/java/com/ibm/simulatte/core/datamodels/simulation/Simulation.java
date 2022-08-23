package com.ibm.simulatte.core.datamodels.simulation;

import com.ibm.simulatte.core.datamodels.analytic.KPI;
import com.ibm.simulatte.core.datamodels.analytic.Metric;
import com.ibm.simulatte.core.datamodels.decisionService.DecisionService;
import com.ibm.simulatte.core.datamodels.data.DataSink;
import com.ibm.simulatte.core.datamodels.data.DataSource;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simulation")
public class Simulation implements Serializable {

    //@Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private int uid;

    @NonNull
    private int userUid;

    @NonNull
    private String name ;

    private String description ;

    @CreationTimestamp
    private Date createDate;

    @UpdateTimestamp
    private Date lastUpdateDate ;

    @NonNull
    private Boolean trace;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private DataSource dataSource ;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private DataSink dataSink ;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private DecisionService decisionService ;

    @NonNull
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Metric> metrics;

    @NonNull
    @OneToMany(cascade = CascadeType.ALL)
    private Set<KPI> kpi;

    @OneToOne(cascade = CascadeType.ALL)
    private SimulationReport simulationReport;

    public void addMetrics(List<Metric> metric){

    }

    public void addKPI(List<KPI> kpi){

    }

}
