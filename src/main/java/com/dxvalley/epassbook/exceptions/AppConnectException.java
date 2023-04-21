package com.dxvalley.epassbook.exceptions;

public class AppConnectException extends RuntimeException {
    public AppConnectException() {
        super("Sorry, service is not available due to an internal error. Please try again later. Thank you.");
    }
}