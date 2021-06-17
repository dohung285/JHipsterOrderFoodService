package com.dohung.orderfood.web.rest.response;

public class ObjectMenuChildDto {

    private String icon;
    private String label;
    private String command;

    public ObjectMenuChildDto() {}

    public ObjectMenuChildDto(String icon, String label, String command) {
        this.icon = icon;
        this.label = label;
        this.command = command;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
