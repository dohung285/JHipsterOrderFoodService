package com.dohung.orderfood.web.rest.response;

import java.time.LocalDateTime;

public class ObjectOrderStatusResponseDto {

    private Integer id;

    private String address;

    private String phone;

    private String username;

    private LocalDateTime dateOrder;

    private String note;

    private Integer status;

    public ObjectOrderStatusResponseDto() {}

    public ObjectOrderStatusResponseDto(
        Integer id,
        String address,
        String phone,
        String username,
        LocalDateTime dateOrder,
        String note,
        Integer status
    ) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.dateOrder = dateOrder;
        this.note = note;
        this.status = status;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return (
            "ObjectOrderStatusResponseDto{" +
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
            ", status=" +
            status +
            '}'
        );
    }
}
