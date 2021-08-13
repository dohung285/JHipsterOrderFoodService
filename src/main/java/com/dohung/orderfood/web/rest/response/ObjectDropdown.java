package com.dohung.orderfood.web.rest.response;

public class ObjectDropdown {

    private String code;
    private String name;

    public ObjectDropdown() {}

    public ObjectDropdown(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
        return "ObjectDropdown{" + "code='" + code + '\'' + ", name='" + name + '\'' + '}';
    }
}
