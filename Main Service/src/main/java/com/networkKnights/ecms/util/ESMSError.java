package com.networkKnights.ecms.util;

public class ESMSError {
    private String errorMsg;

    public ESMSError(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
