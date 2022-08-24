package com.ibm.simulatte.offline.configs.shutdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


public class ShutdownManager {

    @Autowired
    private ApplicationContext appContext;

    /*
     * Invoke with `0` to indicate no error or different code to indicate
     * abnormal exit. es: shutdownManager.initiateShutdown(0);
     **/

    public void initiateShutdown(String message){
        System.exit(SpringApplication.exit(appContext, () -> {
            System.out.println("Error when Simulatte running: "+message);
            return -1;
        }));
    }
    public void initiateShutdown(int returnCode, String message){
        System.exit(SpringApplication.exit(appContext, () -> {
            System.out.println("Error when Simulatte running: "+message);
            return returnCode;
        }));
    }
}
