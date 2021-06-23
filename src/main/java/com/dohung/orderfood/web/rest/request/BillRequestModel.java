package com.dohung.orderfood.web.rest.request;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BillRequestModel {

    private Integer orderId;

    private BigDecimal totalMoney;

    private String username;

    public BillRequestModel() {}

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "BillRequestModel{" + "orderId=" + orderId + ", totalMoney=" + totalMoney + ", username='" + username + '\'' + '}';
    }
}
