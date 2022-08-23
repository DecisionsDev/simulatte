package com.ibm.simulatte.core.datamodels.data;

public enum FileType {
    CSV, JSON, XML, PARQUET, AVRO ;

    public static boolean isValidFileType(String fileType)
    {
        for(FileType type:values())
            if (type.name().equals(fileType))
                return true;
        return false;
    }
}
