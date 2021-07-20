package com.dohung.orderfood.web.rest.response;

import java.util.List;

public class ObjectTreePer {

    private Integer key;
    private String label;
    private List<ObjectChildrenTree> children;

    public ObjectTreePer() {}

    public ObjectTreePer(Integer key, String label) {
        this.key = key;
        this.label = label;
    }

    public ObjectTreePer(List<ObjectChildrenTree> children) {
        this.children = children;
    }

    public ObjectTreePer(Integer key, String label, List<ObjectChildrenTree> children) {
        this.key = key;
        this.label = label;
        this.children = children;
    }

    public List<ObjectChildrenTree> getChildren() {
        return children;
    }

    public void setChildren(List<ObjectChildrenTree> children) {
        this.children = children;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
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
        return "ObjectTreePer{" + "key=" + key + ", label='" + label + '\'' + ", children=" + children + '}';
    }
}
