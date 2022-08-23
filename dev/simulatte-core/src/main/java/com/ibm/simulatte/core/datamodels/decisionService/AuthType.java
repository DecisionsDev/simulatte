package com.ibm.simulatte.core.datamodels.decisionService;

public enum AuthType {
    NO_AUTH, BASIC_AUTH, API_KEY, BEARER_TOKEN, O_AUTH_2, ZEN_TOKEN ;

    public static boolean isValid(AuthType authType)
    {
        for(AuthType type:values())
            if (type.name().equals(authType))
                return true;
        return false;
    }
}
