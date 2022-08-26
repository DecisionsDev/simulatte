package com.ibm.simulatte.core.datamodels.decisionService;

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
@Entity
@Table(name = "decision_service")
public class DecisionService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @NonNull
    private Type type ;

    @NonNull
    @Column(length = 1024)
    private String endPoint;

    @NonNull
    private AuthType authType;

    private String operationName;

    //BASIC AUTH
    private String username ;
    private String password ;

    //API KEY
    private String key;
    private String value;

    //TOKEN AUTH (BEARER or ZEN)
    private String token;

    //O AUTH 2.0
    private String headerPrefix;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "decisionService")
    private Simulation simulation;

}
