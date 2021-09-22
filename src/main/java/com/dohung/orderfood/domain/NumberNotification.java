package com.dohung.orderfood.domain;

import javax.persistence.*;

@Entity
@Table(name = "number_notification")
public class NumberNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number")
    private Integer number;

    public NumberNotification() {}

    public NumberNotification(Integer id, Integer number) {
        this.id = id;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "NumberNotification{" + "id=" + id + ", number=" + number + '}';
    }
}
