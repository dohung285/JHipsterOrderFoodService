package com.dohung.orderfood.web.rest.request;

import java.math.BigDecimal;

public class UpdateDiscountRequestModel {

    private String name;
    private Integer percent;

    public UpdateDiscountRequestModel() {}

    public UpdateDiscountRequestModel(String name, Integer percent) {
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "UpdateDiscountRequestModel{" + "name='" + name + '\'' + ", percent=" + percent + '}';
    }
}
