package com.ibm.simulatte.core.datamodels.simulation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simulation_report")
public class SimulationReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private int uid;

    @NonNull
    private int simulationUid;

    private int numberOfRuns;

    private int numberOfRunsInProcess;

    @JsonIgnore
    @OneToOne(mappedBy = "simulationReport")
    private Simulation simulation;

    @Transient
    private List<Integer> runsUids;

}
