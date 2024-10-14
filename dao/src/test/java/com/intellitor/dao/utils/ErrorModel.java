package com.intellitor.dao.utils;

import java.util.Map;

public class ErrorModel {

    private String error;

    private String errorCode;

    private String uuid;

    private Map<String, String> fieldErrors;

    public ErrorModel() {
    }

    public ErrorModel(String error, String errorCode, String uuid, Map<String, String> fieldErrors) {
        this.error = error;
        this.errorCode = errorCode;
        this.uuid = uuid;
        this.fieldErrors = fieldErrors;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}