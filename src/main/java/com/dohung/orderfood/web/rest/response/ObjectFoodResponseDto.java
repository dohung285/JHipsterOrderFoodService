package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ObjectFoodResponseDto {

    private Integer id;

    private String name;

    private String path;

    private BigDecimal price;

    public ObjectFoodResponseDto() {}

    public ObjectFoodResponseDto(Integer id, String name, String path, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ObjectFoodResponseDto{" + "id=" + id + ", name='" + name + '\'' + ", path='" + path + '\'' + '}';
    }
}
