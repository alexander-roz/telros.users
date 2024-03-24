package com.telros.users.dto;

public class Request {
    private boolean result;
    private String error;

    public Request(boolean result){
        this.result = result;
    }

    public Request(boolean result, String error){
        this.result = result;
        this.error = error;
    }
}
