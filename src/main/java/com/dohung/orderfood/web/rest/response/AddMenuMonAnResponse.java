package com.dohung.orderfood.web.rest.response;

public class AddMenuMonAnResponse {

    private String name;
    private String icon;
    private Integer foodGroupId;
    private String link;

    public AddMenuMonAnResponse() {}

    public AddMenuMonAnResponse(String name, String icon, Integer foodGroupId, String link) {
        this.name = name;
        this.icon = icon;
        this.foodGroupId = foodGroupId;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getFoodGroupId() {
        return foodGroupId;
    }

    public void setFoodGroupId(Integer foodGroupId) {
        this.foodGroupId = foodGroupId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return (
            "AddMenuMonAnResponse{" +
            "name='" +
            name +
            '\'' +
            ", icon='" +
            icon +
            '\'' +
            ", foodGroupId=" +
            foodGroupId +
            ", link='" +
            link +
            '\'' +
            '}'
        );
    }
}
