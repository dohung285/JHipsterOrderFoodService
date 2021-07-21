package com.dohung.orderfood.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "action_system")
public class ActionSystem {

    @Id
    private String id;

    @Column(name = "funct_id")
    private Integer functId;

    @Column(name = "action")
    private String action;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    public ActionSystem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFunctId() {
        return functId;
    }

    public void setFunctId(Integer functId) {
        this.functId = functId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return (
            "ActionSystem{" +
            "id=" +
            id +
            ", functId=" +
            functId +
            ", action='" +
            action +
            '\'' +
            ", createdBy='" +
            createdBy +
            '\'' +
            ", createdDate=" +
            createdDate +
            ", lastModifiedBy='" +
            lastModifiedBy +
            '\'' +
            ", lastModifiedDate=" +
            lastModifiedDate +
            '}'
        );
    }
}
