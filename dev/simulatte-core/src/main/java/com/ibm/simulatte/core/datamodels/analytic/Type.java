package com.ibm.simulatte.core.datamodels.analytic;

public enum Type {
    SPARK_SQL, PYTHON, SQL, HDL, JSON ;

    public static boolean isValid(Type type)
    {
        for(Type execType:values())
            if (execType.equals(type))
                return true;
        return false;
    }
}