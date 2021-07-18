package com.dohung.orderfood.web.rest.response;

public class ObjectRoleResponseDto {

    private String id;
    private String email;
    private String username;
    private Boolean hasRoleAdmin;

    public ObjectRoleResponseDto() {}

    public ObjectRoleResponseDto(String id, String email, String username, Boolean hasRoleAdmin) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.hasRoleAdmin = hasRoleAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getHasRoleAdmin() {
        return hasRoleAdmin;
    }

    public void setHasRoleAdmin(Boolean hasRoleAdmin) {
        this.hasRoleAdmin = hasRoleAdmin;
    }

    @Override
    public String toString() {
        return (
            "ObjectRoleResponseDto{" +
            "id='" +
            id +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", hasRoleAdmin=" +
            hasRoleAdmin +
            '}'
        );
    }
}
