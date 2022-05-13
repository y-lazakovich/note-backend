package com.cherishdev.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class HttpResponse<T> implements Serializable {

    protected String timestamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Collection<? extends T> data;

    public HttpResponse() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public HttpResponse<T> setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse<T> setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpResponse<T> setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public HttpResponse<T> setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HttpResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public HttpResponse<T> setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
        return this;
    }

    public Collection<? extends T> getData() {
        return data;
    }

    public HttpResponse<T> setData(Collection<? extends T> data) {
        this.data = data;
        return this;
    }
}
