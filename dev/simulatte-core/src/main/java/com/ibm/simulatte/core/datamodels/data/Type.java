package com.ibm.simulatte.core.datamodels.data;

public enum Type {
    DATA_SOURCE, DATA_SINK;

    public static boolean isValid(String dataType)
    {
        for(Type type:values())
            if (type.name().equals(dataType))
                return true;
        return false;
    }
}
