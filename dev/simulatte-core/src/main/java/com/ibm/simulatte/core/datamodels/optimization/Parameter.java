package com.ibm.simulatte.core.datamodels.optimization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.simulatte.core.datamodels.run.Run;
import com.ibm.simulatte.core.datamodels.simulation.Simulation;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "optimization_parameter")
public class Parameter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @NonNull
    @JsonProperty("name")
    private String name ;

    @NonNull
    @JsonProperty("value")
    private String value ;

    @NonNull
    @JsonProperty("type")
    private String type ;

    @JsonProperty("description")
    private String description ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="run_uid")
    private Run run;
}
