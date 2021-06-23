package com.dohung.orderfood.web.rest.request;

public class ObjectFoodDetail {

    private Integer imageId;
    private String description;

    public ObjectFoodDetail() {}

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
