package com.dohung.orderfood.web.rest.request;

public class UserChangePasswordRequestModel {

    private String currentPassword;
    private String username;
    private String newPassword;

    public UserChangePasswordRequestModel() {}

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return (
            "UserChangePasswordRequestModel{" +
            "currentPassword='" +
            currentPassword +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", newPassword='" +
            newPassword +
            '\'' +
            '}'
        );
    }
}
