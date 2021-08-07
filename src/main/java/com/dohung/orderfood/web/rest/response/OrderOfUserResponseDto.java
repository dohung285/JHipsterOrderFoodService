package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OrderOfUserResponseDto {

    private Integer orderId;
    private Date dateOrder;
    private Integer status;
    private BigDecimal totalMoney;

    private List<ObjectOrderDetailOfUserResponseDto> listObjectOrderDetailOfUserResponseDto;

    public OrderOfUserResponseDto() {}

    public OrderOfUserResponseDto(Integer orderId, Date dateOrder, Integer status, BigDecimal totalMoney) {
        this.orderId = orderId;
        this.dateOrder = dateOrder;
        this.status = status;
        this.totalMoney = totalMoney;
    }

    public List<ObjectOrderDetailOfUserResponseDto> getListObjectOrderDetailOfUserResponseDto() {
        return listObjectOrderDetailOfUserResponseDto;
    }

    public void setListObjectOrderDetailOfUserResponseDto(List<ObjectOrderDetailOfUserResponseDto> listObjectOrderDetailOfUserResponseDto) {
        this.listObjectOrderDetailOfUserResponseDto = listObjectOrderDetailOfUserResponseDto;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return (
            "OrderOfUserResponseDto{" +
            "orderId=" +
            orderId +
            ", dateOrder=" +
            dateOrder +
            ", status=" +
            status +
            ", totalMoney=" +
            totalMoney +
            ", listObjectOrderDetailOfUserResponseDto=" +
            listObjectOrderDetailOfUserResponseDto +
            '}'
        );
    }
}
