package com.ibm.simulatte.core.datamodels.user;

public enum Role {
    ADMIN, USER;

    public static boolean isValid(Role role)
    {
        for(Role type:values())
            if (type.name().equals(role))
                return true;
        return false;
    }
}
