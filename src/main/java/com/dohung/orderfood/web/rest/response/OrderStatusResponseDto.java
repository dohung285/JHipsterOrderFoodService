package com.dohung.orderfood.web.rest.response;

import java.io.Serializable;

public class OrderStatusResponseDto implements Serializable {

    private Integer id;

    private Integer orderId;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public OrderStatusResponseDto() {}

    @Override
    public String toString() {
        return "OrderStatusResponseDto{" + "id=" + id + ", orderId=" + orderId + ", status=" + status + '}';
    }
}
