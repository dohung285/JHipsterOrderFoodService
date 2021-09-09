package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;

public class OrderDetailReport {

    private Integer id;
    private String name;
    private Integer amount;
    private Integer percent;
    private BigDecimal money;

    public OrderDetailReport() {
        super();
    }

    public OrderDetailReport(Integer id, String name, Integer amount, Integer percent, BigDecimal money) {
        super();
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.percent = percent;
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "OrderDetail [id=" + id + ", name=" + name + ", amount=" + amount + ", percent=" + percent + ", money=" + money + "]";
    }
}
