package com.ibm.simulatte.core.datamodels.executor;

public enum Type {
    JSE, SPARK_STANDALONE, SPARK_CLUSTER ;

    public static boolean isValid(Type type)
    {
        for(Type execType:values())
            if (execType.equals(type))
                return true;
        return false;
    }
}
