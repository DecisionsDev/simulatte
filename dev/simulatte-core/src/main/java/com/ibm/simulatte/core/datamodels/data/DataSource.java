package com.ibm.simulatte.core.datamodels.data;

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
@Table(name = "data")
public class DataSource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @JsonIgnore
    private final String type = "DATA_SOURCE" ;

    @NonNull
    private String format ;

    @NonNull
    private String uri ;

    private String username ;
    private String password ;

    //private JSONObject properties;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "dataSource")
    private Simulation simulation;

}
