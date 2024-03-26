package com.networkKnights.ecms_auth.entity;

public class ESMSError {
    private String status;

    public ESMSError(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
