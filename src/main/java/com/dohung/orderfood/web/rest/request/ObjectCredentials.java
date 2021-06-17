package com.dohung.orderfood.web.rest.request;

import jdk.jfr.SettingDefinition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ObjectCredentials {

    private String type;
    private String value;
    private Boolean temporary;
}
