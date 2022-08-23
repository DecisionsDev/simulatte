package com.ibm.simulatte.core.datamodels.run;

public enum RunStatusType {
    NOT_CREATED, CREATED, STARTED, PAUSED, RUNNING, STOPPED, FINISHED ;
    public static boolean isValid(String status)
    {
        for(RunStatusType runStatusType :values())
            if (runStatusType.name().equals(status))
                return true;
        return false;
    }
}
