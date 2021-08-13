package com.dohung.orderfood.web.rest.response;

import java.math.BigInteger;
import java.util.List;

public class ObjectDatasetPieChart {

    List<BigInteger> data;

    List<String> backgroundColor;

    public ObjectDatasetPieChart() {}

    public ObjectDatasetPieChart(List<BigInteger> data, List<String> backgroundColor) {
        this.data = data;
        this.backgroundColor = backgroundColor;
    }

    public List<BigInteger> getData() {
        return data;
    }

    public void setData(List<BigInteger> data) {
        this.data = data;
    }

    public List<String> getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(List<String> backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "ObjectDatasetPieChart{" + "data=" + data + ", backgroundColor=" + backgroundColor + '}';
    }
}
