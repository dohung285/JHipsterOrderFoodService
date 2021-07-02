package com.dohung.orderfood.web.rest.request;

import com.dohung.orderfood.web.rest.response.ObjectOrderDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public class OrderRequestModel {

    private String address;

    private String phone;

    private String username;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateOrder;

    private String note;

    List<ObjectOrderDetail> orderDetails;

    public OrderRequestModel() {}

    public List<ObjectOrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<ObjectOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDateTime dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return (
            "OrderRequestModel{" +
            "address='" +
            address +
            '\'' +
            ", phone='" +
            phone +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", dateOrder=" +
            dateOrder +
            ", note='" +
            note +
            '\'' +
            ", orderDetails=" +
            orderDetails +
            '}'
        );
    }
}
