package com.stefandanila.api.response;

public class ResponseSuccess {
    private final String success;

    public ResponseSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }
}
