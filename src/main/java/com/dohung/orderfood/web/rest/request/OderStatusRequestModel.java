package com.dohung.orderfood.web.rest.request;

public class OderStatusRequestModel {

    private Integer orderId;

    private Integer status;

    public OderStatusRequestModel() {}

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OderStatusRequestModel{" + "orderId=" + orderId + ", status=" + status + '}';
    }
}
