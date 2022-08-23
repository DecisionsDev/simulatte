package com.ibm.simulatte.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("uid")
    private int uid;

    @JsonProperty("firstname")
    private String firstname ;

    @JsonProperty("lastname")
    private String lastname ;

    @JsonProperty("username")
    private String username ;

    @JsonProperty("email")
    @NonNull
    private String email ;

    @JsonProperty("phoneNumber")
    private String phoneNumber ;

    @JsonProperty("password")
    @NonNull
    private String password ;

    @JsonProperty("userRole")
    private String userRole ;

    @JsonIgnore
    private Date createDate ;

}
