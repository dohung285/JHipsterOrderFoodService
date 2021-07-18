package com.dohung.orderfood.web.rest.response;

public class CommentObjectResponseDto {

    private Integer id;

    private String foodName;

    private String content;

    private String username;

    private Integer rating;

    public CommentObjectResponseDto() {}

    public CommentObjectResponseDto(Integer id, String foodName, String content, String username, Integer rating) {
        this.id = id;
        this.foodName = foodName;
        this.content = content;
        this.username = username;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
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
            "CommentObjectResponseDto{" +
            "id=" +
            id +
            ", foodName='" +
            foodName +
            '\'' +
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
