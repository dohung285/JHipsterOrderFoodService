package com.dohung.orderfood.web.rest.response;

import java.time.LocalDateTime;

public class DiscountResponseDto {

    private String name;

    private Integer percent;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public DiscountResponseDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return (
            "DiscountRequestModel{" +
            ", name='" +
            name +
            '\'' +
            ", percent=" +
            percent +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            '}'
        );
    }
}
