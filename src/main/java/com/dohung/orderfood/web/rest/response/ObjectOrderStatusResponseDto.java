package com.dohung.orderfood.web.rest.response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ObjectOrderStatusResponseDto {

    private Integer id;

    private String address;

    private String phone;

    private String username;

    private Date dateOrder;

    private String note;

    private Integer status;

    private List<ObjectChildOrderDetailResponse> listChild;

    public ObjectOrderStatusResponseDto() {}

    public ObjectOrderStatusResponseDto(
        Integer id,
        String address,
        String phone,
        String username,
        Date dateOrder,
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

    public List<ObjectChildOrderDetailResponse> getListChild() {
        return listChild;
    }

    public void setListChild(List<ObjectChildOrderDetailResponse> listChild) {
        this.listChild = listChild;
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

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
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
