package com.dohung.orderfood.web.rest.response;

import java.util.Date;

public class OrderReport {

    private Integer id;
    private String username;
    private String phone;
    private String address;
    private String dateOrder;
    private String note;

    public OrderReport() {
        super();
    }

    public OrderReport(Integer id, String username, String phone, String address, String dateOrder, String note) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.dateOrder = dateOrder;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    @Override
    public String toString() {
        return (
            "Order{" +
            "id=" +
            id +
            ", username='" +
            username +
            '\'' +
            ", phone='" +
            phone +
            '\'' +
            ", address='" +
            address +
            '\'' +
            ", dateOrder=" +
            dateOrder +
            ", note='" +
            note +
            '\'' +
            '}'
        );
    }
}
