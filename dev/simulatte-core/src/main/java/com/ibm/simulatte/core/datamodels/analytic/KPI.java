package com.ibm.simulatte.core.datamodels.analytic;

import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "kpi")
public class KPI implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @NonNull
    @JsonProperty("name")
    private String name ;

    @NonNull
    private Type type ;

    @JsonProperty("description")
    private String description ;

    /*@JsonProperty("type")
    private String ;*/

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="simulation_id")
    private Simulation simulation;

}
