package com.dohung.orderfood.web.rest.response;

public class ObjectReportReturn {

    private Integer id;
    private Integer orderId;
    private String name;
    private String path;

    public ObjectReportReturn() {}

    public ObjectReportReturn(Integer id, Integer orderId, String name, String path) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ObjectReportReturn{" + "id=" + id + ", orderId=" + orderId + ", name='" + name + '\'' + ", path='" + path + '\'' + '}';
    }
}
