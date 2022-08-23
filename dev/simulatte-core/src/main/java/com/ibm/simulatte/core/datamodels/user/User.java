package com.ibm.simulatte.core.datamodels.user;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Id
    private int uid;

    private String firstname ;

    private String lastname ;

    @NonNull
    private String username ;

    private String email ;

    private String phoneNumber ;

    @NonNull
    private String password ;

    @NonNull
    private String userRole ;

    //@CreationTimestamp
    private Date createDate ;

    //@UpdateTimestamp
    private Date lastUpdateDate ;

}
