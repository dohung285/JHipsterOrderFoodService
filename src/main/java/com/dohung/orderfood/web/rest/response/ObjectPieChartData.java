package com.dohung.orderfood.web.rest.response;

import java.util.List;

public class ObjectPieChartData {

    List<String> labels;

    List<ObjectDatasetPieChart> datasets;

    public ObjectPieChartData(List<String> labels, List<ObjectDatasetPieChart> datasets) {
        this.labels = labels;
        this.datasets = datasets;
    }

    public ObjectPieChartData() {}

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<ObjectDatasetPieChart> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<ObjectDatasetPieChart> datasets) {
        this.datasets = datasets;
    }

    @Override
    public String toString() {
        return "ObjectPieChartData{" + "labels=" + labels + ", datasets=" + datasets + '}';
    }
}
