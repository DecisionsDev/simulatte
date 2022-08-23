package com.ibm.simulatte.core.datamodels.executor;

public enum Mode {
    REMOTE, LOCAL ;

    public static boolean isValid(Mode mode)
    {
        for(Mode execMode:values())
            if (execMode.equals(mode))
                return true;
        return false;
    }
}
