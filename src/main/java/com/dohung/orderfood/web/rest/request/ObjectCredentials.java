package com.dohung.orderfood.web.rest.request;

import jdk.jfr.SettingDefinition;
import lombok.*;

public class ObjectCredentials {

    private String type;
    private String value;
    private Boolean temporary;

    public ObjectCredentials() {}

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
        return "ObjectCredentials{" + "type='" + type + '\'' + ", value='" + value + '\'' + ", temporary=" + temporary + '}';
    }
}
