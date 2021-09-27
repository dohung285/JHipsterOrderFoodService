package com.dohung.orderfood.web.rest.response;

public class NumberNotificationResponse {

    private Integer number;

    public NumberNotificationResponse() {}

    public NumberNotificationResponse(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "NumberNotificationResponse{" + "number=" + number + '}';
    }
}
