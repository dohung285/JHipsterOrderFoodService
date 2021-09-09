package com.dohung.orderfood.web.rest.request;

import java.math.BigDecimal;

public class UpdateFoodRequestModel {

    private String name;
    private BigDecimal price;

    public UpdateFoodRequestModel() {}

    public UpdateFoodRequestModel(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "UpdateFoodRequestModel{" + "name='" + name + '\'' + ", price=" + price + '}';
    }
}
