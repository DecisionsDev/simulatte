package com.ibm.simulatte.core.exception;

public class NoDecisionServiceFoundException extends Exception {
    public NoDecisionServiceFoundException(){}

    public NoDecisionServiceFoundException(String message){
        super(message);
    }

    public NoDecisionServiceFoundException(Throwable cause){
        super(cause);
    }

    public NoDecisionServiceFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
