package com.ibm.simulatte.core.datamodels.run;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "run_report")
public class RunReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private int uid;

    //@NonNull
    //private int runUid;

    //@Enumerated(EnumType.ORDINAL)
    @NonNull
    private RunStatusType status;

    private int numberOfDecisions;

    private int numberOfRequests;

    private float progress;

    private int duration;

    @Getter(value = AccessLevel.NONE)
    private float numberOfDecisionsPerSecond;

    @JsonIgnore
    @Transient
    private RunAction action = RunAction.NONE;  // Put it in Run Object (and store in database) to avoid static INTERRUPT variable

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "runReport")
    private Run run;

    public float getNumberOfDecisionsPerSecond() {
        if(this.getDuration()==0) return -1;
        return (float) numberOfDecisions*1000/duration;
    }

}
