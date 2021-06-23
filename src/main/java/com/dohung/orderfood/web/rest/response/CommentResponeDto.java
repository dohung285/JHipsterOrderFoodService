package com.dohung.orderfood.web.rest.response;

public class CommentResponeDto {

    private Integer id;

    private Integer foodId;

    private String content;

    private String username;

    private Integer rating;

    private String imageName;

    private String path;

    public CommentResponeDto() {}

    public CommentResponeDto(Integer id, Integer foodId, String content, String username, Integer rating, String imageName, String path) {
        this.id = id;
        this.foodId = foodId;
        this.content = content;
        this.username = username;
        this.rating = rating;
        this.imageName = imageName;
        this.path = path;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
            "CommentResponeDto{" +
            "id=" +
            id +
            ", foodId=" +
            foodId +
            ", content='" +
            content +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", rating=" +
            rating +
            ", imageName='" +
            imageName +
            '\'' +
            ", path='" +
            path +
            '\'' +
            '}'
        );
    }
}
