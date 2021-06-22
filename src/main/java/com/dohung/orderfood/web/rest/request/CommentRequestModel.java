package com.dohung.orderfood.web.rest.request;

public class CommentRequestModel {

    private Integer foodId;

    private String content;

    private String username;

    private Integer rating;

    public CommentRequestModel() {}

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return (
            "CommentRequestModel{" +
            "foodId=" +
            foodId +
            ", content='" +
            content +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", rating=" +
            rating +
            '}'
        );
    }
}
