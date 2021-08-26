package com.dohung.orderfood.web.rest.response;

import com.dohung.orderfood.domain.OrderStatus;
import java.util.Date;

public class ObjectOrderStatusWithDateOrderRespone {

    private OrderStatus orderStatus;
    private Date dateOrder;

    public ObjectOrderStatusWithDateOrderRespone() {}

    public ObjectOrderStatusWithDateOrderRespone(OrderStatus orderStatus, Date dateOrder) {
        this.orderStatus = orderStatus;
        this.dateOrder = dateOrder;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    @Override
    public String toString() {
        return "ObjectOrderStatusWithDateOrderRespone{" + "orderStatus=" + orderStatus + ", dateOrder=" + dateOrder + '}';
    }
}
