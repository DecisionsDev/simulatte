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
public class DataSink implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @JsonIgnore
    private final String type = "DATA_SINK" ;

    @NonNull
    private FileType format ;

    @NonNull
    private String folderPath ;

    private String uri ;

    private String username ;
    private String password ;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "dataSink")
    private Simulation simulation;

    /*@JsonIgnore
    @OneToOne(mappedBy = "dataSink")
    private Run run;*/

}
