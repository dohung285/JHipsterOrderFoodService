package com.dohung.orderfood.web.rest.request;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class MenuRequestModel {

    private Integer id;

    private String name;

    private String roleName;

    private String icon;

    private String link;

    private Integer level;

    private Integer parentId;

    private String createdBy;

    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;
}
