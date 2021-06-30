package com.dohung.orderfood.web.rest.response;

import com.dohung.orderfood.domain.OrderDetail;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private String address;

    private String phone;

    private String username;

    private LocalDateTime dateOrder;

    private String note;

    List<OrderDetailResponseDto> orderDetails;

    public OrderResponseDto() {}

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

    public List<OrderDetailResponseDto> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailResponseDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return (
            "OrderResponseDto{" +
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
