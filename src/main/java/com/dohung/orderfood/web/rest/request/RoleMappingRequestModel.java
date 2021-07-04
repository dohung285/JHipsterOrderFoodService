package com.dohung.orderfood.web.rest.request;

public class RoleMappingRequestModel {

    private String id;
    private String name;

    public RoleMappingRequestModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RoleMappingRequestModel{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
    }
}
