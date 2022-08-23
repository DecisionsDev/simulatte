package com.ibm.simulatte.core.datamodels.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthToken {

    String token ;
    String refreshToken ;
    Long tokenExpirationInSeconds;
    Long refreshTokenExpirationInSeconds;

}
