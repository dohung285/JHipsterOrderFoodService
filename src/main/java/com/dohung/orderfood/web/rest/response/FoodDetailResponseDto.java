package com.dohung.orderfood.web.rest.response;

import com.dohung.orderfood.domain.FoodIdentity;

public class FoodDetailResponseDto {

    private FoodIdentity id;

    private String desciption;

    public FoodDetailResponseDto(FoodIdentity id, String desciption) {
        this.id = id;
        this.desciption = desciption;
    }

    public FoodDetailResponseDto() {}

    public FoodIdentity getId() {
        return id;
    }

    public void setId(FoodIdentity id) {
        this.id = id;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
