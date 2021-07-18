package com.dohung.orderfood.web.rest.request;

public class ChangePasswordKeycloak {

    private String type;
    private String value;
    private Boolean temporary;

    public ChangePasswordKeycloak() {}

    public ChangePasswordKeycloak(String type, String value, Boolean temporary) {
        this.type = type;
        this.value = value;
        this.temporary = temporary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    @Override
    public String toString() {
        return "ChangePasswordKeycloak{" + "type='" + type + '\'' + ", value='" + value + '\'' + ", temporary=" + temporary + '}';
    }
}
