package com.dohung.orderfood.web.rest.request;

import java.io.Serializable;
import lombok.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
public class CreateRoleRequestModel {

    private String name;

    public CreateRoleRequestModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
