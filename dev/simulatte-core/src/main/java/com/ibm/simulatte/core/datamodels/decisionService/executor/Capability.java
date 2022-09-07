package com.ibm.simulatte.core.datamodels.decisionService.executor;

public enum Capability {
    ODM, ADS, RHDM ;

    public static boolean isValid(Capability capability)
    {
        for(Capability execCapability:values())
            if (execCapability.equals(capability))
                return true;
        return false;
    }
}
