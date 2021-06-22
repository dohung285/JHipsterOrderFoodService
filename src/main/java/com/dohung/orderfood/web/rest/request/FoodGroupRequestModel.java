package com.dohung.orderfood.web.rest.request;

public class FoodGroupRequestModel {

    private String name;

    public FoodGroupRequestModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FoodGroupRequestModel{" + "name='" + name + '\'' + '}';
    }
}
