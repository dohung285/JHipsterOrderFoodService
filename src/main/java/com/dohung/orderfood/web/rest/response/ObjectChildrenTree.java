package com.dohung.orderfood.web.rest.response;

public class ObjectChildrenTree {

    private String key;
    private String label;

    public ObjectChildrenTree() {}

    public ObjectChildrenTree(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ObjectChildrenTree{" + "key=" + key + ", label='" + label + '\'' + '}';
    }
    //    private Integer key;
    //    private String label;
    //
    //    public ObjectChildrenTree() {
    //    }
    //
    //    public ObjectChildrenTree(Integer key, String label) {
    //        this.key = key;
    //        this.label = label;
    //    }
    //
    //    public Integer getKey() {
    //        return key;
    //    }
    //
    //    public void setKey(Integer key) {
    //        this.key = key;
    //    }
    //
    //    public String getLabel() {
    //        return label;
    //    }
    //
    //    public void setLabel(String label) {
    //        this.label = label;
    //    }
    //
    //    @Override
    //    public String toString() {
    //        return "ObjectChildrenTree{" +
    //            "key=" + key +
    //            ", label='" + label + '\'' +
    //            '}';
    //    }
}
