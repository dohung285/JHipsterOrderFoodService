package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;
import java.util.List;

public class FoodByCatalogResponseDto {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer percent;
    private String path;

    private List<String> listImage;

    public FoodByCatalogResponseDto() {}

    public FoodByCatalogResponseDto(Integer id, String name, BigDecimal price, String description, Integer percent, String path) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.percent = percent;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    @Override
    public String toString() {
        return (
            "FoodByCatalogResponseDto{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", price=" +
            price +
            ", description='" +
            description +
            '\'' +
            ", percent=" +
            percent +
            ", path='" +
            path +
            '\'' +
            ", listImage=" +
            listImage +
            '}'
        );
    }
}
