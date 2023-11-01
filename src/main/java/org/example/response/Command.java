package org.example.response;

import org.springframework.web.bind.annotation.ResponseBody;


public class Command {
    private String message;
    private boolean isSuccessful;
    private String jwtToken;
    public Command(String message, boolean isSuccessful) {
        this.message = message;
        this.isSuccessful = isSuccessful;
    }
    public Command(String message, boolean isSuccessful,String jwtToken) {
        this.message = message;
        this.isSuccessful = isSuccessful;
        this.jwtToken = jwtToken;
    }
    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
