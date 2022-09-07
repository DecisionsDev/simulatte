package com.ibm.simulatte.core.datamodels.decisionService.executor;

import com.ibm.simulatte.core.datamodels.run.Run;
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
@Table(name = "executor")
public class Executor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @NonNull
    private Type type ;

    @NonNull
    private Mode mode;

    @NonNull
    private Capability capability;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "executor")
    private Run run;
}
