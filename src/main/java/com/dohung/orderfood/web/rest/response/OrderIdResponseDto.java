package com.dohung.orderfood.web.rest.response;

public class OrderIdResponseDto {

    private Integer id;

    public OrderIdResponseDto() {}

    public OrderIdResponseDto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderIdResponseDto{" + "id=" + id + '}';
    }
}
