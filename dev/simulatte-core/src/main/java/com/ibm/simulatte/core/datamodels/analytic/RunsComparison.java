package com.ibm.simulatte.core.datamodels.analytic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "runs_comparison")
public class RunsComparison implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    @JsonIgnore
    private int uid;

    @NonNull
    private int firstRunUid ;

    @NonNull
    private int secondRunUid ;

    @NonNull
    private String notebookUri ;

}
