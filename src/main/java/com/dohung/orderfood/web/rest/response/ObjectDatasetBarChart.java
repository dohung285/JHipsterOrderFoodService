package com.dohung.orderfood.web.rest.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class ObjectDatasetBarChart {

    private List<BigDecimal> data;

    private String label;

    private String backgroundColor;

    public ObjectDatasetBarChart() {}

    public ObjectDatasetBarChart(List<BigDecimal> data, String label, String backgroundColor) {
        this.data = data;
        this.label = label;
        this.backgroundColor = backgroundColor;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return (
            "ObjectDatasetBarChart{" + "data=" + data + ", label='" + label + '\'' + ", backgroundColor='" + backgroundColor + '\'' + '}'
        );
    }
}
