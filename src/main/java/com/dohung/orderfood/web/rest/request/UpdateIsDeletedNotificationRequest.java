package com.dohung.orderfood.web.rest.request;

public class UpdateIsDeletedNotificationRequest {

    private Integer isDeleted;

    public UpdateIsDeletedNotificationRequest() {}

    public UpdateIsDeletedNotificationRequest(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "UpdateIsDeletedNotificationRequest{" + "isDeleted=" + isDeleted + '}';
    }
}
