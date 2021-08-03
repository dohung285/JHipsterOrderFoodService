package com.dohung.orderfood.web.rest.response;

public class DiscountObjectResponseDto {

    private int code;

    private String name;

    public DiscountObjectResponseDto() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DiscountObjectResponseDto{" + "code=" + code + ", name='" + name + '\'' + '}';
    }
}
