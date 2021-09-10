package com.dohung.orderfood.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "order_food_detail")
public class OrderDetail {

    @EmbeddedId
    private OrderIdentity id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    public OrderDetail() {}

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderIdentity getId() {
        return id;
    }

    public void setId(OrderIdentity id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return (
            "OrderDetail{" +
            "id=" +
            id +
            ", amount=" +
            amount +
            ", price=" +
            price +
            ", money=" +
            money +
            ", createdBy='" +
            createdBy +
            '\'' +
            ", createdDate=" +
            createdDate +
            ", lastModifiedBy='" +
            lastModifiedBy +
            '\'' +
            ", lastModifiedDate=" +
            lastModifiedDate +
            '}'
        );
    }
}
