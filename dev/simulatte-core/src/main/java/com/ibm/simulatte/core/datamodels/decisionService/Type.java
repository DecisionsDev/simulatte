package com.ibm.simulatte.core.datamodels.decisionService;

public enum Type {
    ODM, ADS, RHDM ;

    public static boolean isValidType(Type decisionServiceType)
    {
        for(Type type:values())
            if (type.name().equals(decisionServiceType.name()))
                return true;
        return false;
    }
}
