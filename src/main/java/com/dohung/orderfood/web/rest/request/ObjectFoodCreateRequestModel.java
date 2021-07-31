package com.dohung.orderfood.web.rest.request;

import java.math.BigDecimal;

public class ObjectFoodCreateRequestModel {

    private String name;

    private BigDecimal price;

    private Integer groupId;

    private String desciption;

    //    private MultipartFile[] files;

    public ObjectFoodCreateRequestModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    @Override
    public String toString() {
        return (
            "ObjectFoodCreateRequestModel{" +
            "name='" +
            name +
            '\'' +
            ", price=" +
            price +
            ", groupId=" +
            groupId +
            ", desciption='" +
            desciption +
            '\'' +
            '}'
        );
    }
    //    public MultipartFile[] getFiles() {
    //        return files;
    //    }
    //
    //    public void setFiles(MultipartFile[] files) {
    //        this.files = files;
    //    }
    //
    //    @Override
    //    public String toString() {
    //        return "ObjectFoodCreateRequestModel{" +
    //            "name='" + name + '\'' +
    //            ", price=" + price +
    //            ", groupId=" + groupId +
    //            ", desciption='" + desciption + '\'' +
    //            ", files=" + Arrays.toString(files) +
    //            '}';
    //    }
}
