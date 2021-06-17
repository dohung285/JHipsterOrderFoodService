package com.dohung.orderfood.web.rest.response;

import java.util.List;

public class ObjectMenuParentDto {

    private String icon;
    private String label;
    private List<ObjectMenuChildDto> items;

    public ObjectMenuParentDto() {}

    public ObjectMenuParentDto(String icon, String label, List<ObjectMenuChildDto> items) {
        this.icon = icon;
        this.label = label;
        this.items = items;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ObjectMenuChildDto> getItems() {
        return items;
    }

    public void setItems(List<ObjectMenuChildDto> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ObjectMenuParentDto{" + "icon='" + icon + '\'' + ", label='" + label + '\'' + ", items=" + items + '}';
    }
}
