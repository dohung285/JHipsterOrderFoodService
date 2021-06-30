package com.dohung.orderfood.web.rest.request;

import com.dohung.orderfood.domain.OrderDetail;
import com.dohung.orderfood.web.rest.response.ObjectOrderDetail;
import java.time.LocalDateTime;
import java.util.List;

public class OrderRequestModel {

    private Integer id;

    private String address;

    private String phone;

    private String username;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
            "id=" +
            id +
            ", address='" +
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
