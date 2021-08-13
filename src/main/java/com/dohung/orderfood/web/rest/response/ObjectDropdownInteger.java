package com.dohung.orderfood.web.rest.response;

public class ObjectDropdownInteger {

    private String code;
    private Integer name;

    public ObjectDropdownInteger() {}

    public ObjectDropdownInteger(String code, Integer name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ObjectDropdownInteger{" + "code='" + code + '\'' + ", name=" + name + '}';
    }
}
