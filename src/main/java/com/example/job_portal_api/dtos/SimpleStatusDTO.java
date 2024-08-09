package com.example.job_portal_api.dtos;

public class SimpleStatusDTO {
    private String message;

    private Integer status;

    private Object body;

    public SimpleStatusDTO() {
    }

    public SimpleStatusDTO(String message, Integer status, Object body) {
        this.message = message;
        this.status = status;
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

}
