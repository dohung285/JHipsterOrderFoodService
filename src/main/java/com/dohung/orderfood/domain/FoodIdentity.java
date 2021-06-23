package com.dohung.orderfood.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class FoodIdentity implements Serializable {

    private Integer foodId;
    private Integer imageId;

    public FoodIdentity() {}

    public FoodIdentity(Integer foodId, Integer imageId) {
        this.foodId = foodId;
        this.imageId = imageId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodIdentity that = (FoodIdentity) o;
        return Objects.equals(foodId, that.foodId) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, imageId);
    }

    @Override
    public String toString() {
        return "FoodIdentity{" + "foodId=" + foodId + ", imageId=" + imageId + '}';
    }
}
