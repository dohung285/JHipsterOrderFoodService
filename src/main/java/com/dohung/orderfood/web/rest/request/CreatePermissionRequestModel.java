package com.dohung.orderfood.web.rest.request;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class CreatePermissionRequestModel {

    private String username;

    private List<String> actionIds;

    private Map<String, Object> currentPermission;

    public CreatePermissionRequestModel() {}

    public Map<String, Object> getCurrentPermission() {
        return currentPermission;
    }

    public void setCurrentPermission(Map<String, Object> currentPermission) {
        this.currentPermission = currentPermission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getActionIds() {
        return actionIds;
    }

    public void setActionIds(List<String> actionIds) {
        this.actionIds = actionIds;
    }

    @Override
    public String toString() {
        return (
            "CreatePermissionRequestModel{" +
            "username='" +
            username +
            '\'' +
            ", actionIds=" +
            actionIds +
            ", currentPermission='" +
            currentPermission +
            '\'' +
            '}'
        );
    }
}
