package com.dohung.orderfood.web.rest.request;

import java.io.Serializable;
import java.util.List;
import lombok.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
public class UserDetailsRequestModel implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    //    private String password;
    private Boolean enabled;
    private String username;
    private List<ObjectCredentials> credentials;

    public UserDetailsRequestModel() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ObjectCredentials> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<ObjectCredentials> credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return (
            "UserDetailsRequestModel{" +
            "firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", enabled=" +
            enabled +
            ", username='" +
            username +
            '\'' +
            ", credentials=" +
            credentials +
            '}'
        );
    }
}
