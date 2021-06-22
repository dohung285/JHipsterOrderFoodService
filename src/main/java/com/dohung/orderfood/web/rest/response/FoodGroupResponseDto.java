package com.dohung.orderfood.web.rest.response;

public class FoodGroupResponseDto {

    private Integer id;

    private String name;

    public FoodGroupResponseDto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FoodGroupResponseDto{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
