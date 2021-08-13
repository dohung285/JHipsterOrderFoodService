package com.dohung.orderfood.web.rest.response;

import java.util.List;

public class ObjectBarChartData {

    List<String> labels;

    List<ObjectDatasetBarChart> datasets;

    public ObjectBarChartData() {}

    public ObjectBarChartData(List<String> labels, List<ObjectDatasetBarChart> datasets) {
        this.labels = labels;
        this.datasets = datasets;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<ObjectDatasetBarChart> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<ObjectDatasetBarChart> datasets) {
        this.datasets = datasets;
    }

    @Override
    public String toString() {
        return "ObjectBarChartData{" + "labels=" + labels + ", datasets=" + datasets + '}';
    }
}
