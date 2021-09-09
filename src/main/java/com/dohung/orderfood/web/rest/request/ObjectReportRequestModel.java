package com.dohung.orderfood.web.rest.request;

import com.dohung.orderfood.web.rest.response.OrderDetailReport;
import com.dohung.orderfood.web.rest.response.OrderReport;
import java.math.BigDecimal;
import java.util.List;

public class ObjectReportRequestModel {

    private OrderReport order;
    private List<OrderDetailReport> listOrderDetail;
    private BigDecimal totalMoney;

    public ObjectReportRequestModel() {}

    public ObjectReportRequestModel(OrderReport order, List<OrderDetailReport> listOrderDetail, BigDecimal totalMoney) {
        this.order = order;
        this.listOrderDetail = listOrderDetail;
        this.totalMoney = totalMoney;
    }

    public OrderReport getOrder() {
        return order;
    }

    public void setOrder(OrderReport order) {
        this.order = order;
    }

    public List<OrderDetailReport> getListOrderDetail() {
        return listOrderDetail;
    }

    public void setListOrderDetail(List<OrderDetailReport> listOrderDetail) {
        this.listOrderDetail = listOrderDetail;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "ObjectReportRequestModel{" + "order=" + order + ", listOrderDetail=" + listOrderDetail + ", totalMoney=" + totalMoney + '}';
    }
}
