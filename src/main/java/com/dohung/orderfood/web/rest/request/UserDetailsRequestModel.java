package com.dohung.orderfood.web.rest.request;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailsRequestModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean enabled;
    private String username;
    private List<ObjectCredentials> credentials;
}
