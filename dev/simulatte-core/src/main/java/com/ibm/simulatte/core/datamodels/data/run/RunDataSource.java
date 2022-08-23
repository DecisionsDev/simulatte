package com.ibm.simulatte.core.datamodels.data.run;

import com.ibm.simulatte.core.datamodels.run.Run;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "run_datasource")
public class RunDataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonProperty("uid")
    private int uid;

    @NonNull
    private String format ;

    @NonNull
    private String uri ;

    private String username ;
    private String password ;

    //private JSONObject properties;

    @JsonIgnore
    //@OneToOne(mappedBy = "dataSource")
    private Run run;

}
