package com.dohung.orderfood.web.rest.response;

public class ObjectOrderTrackingReponse {

    //        { status: 'Tiếp nhận đơn', date: '15/10/2020 10:30', icon: 'pi pi-shopping-cart', color: '#9C27B0', image: 'http://localhost:8083/downloadFile/order.png' },

    private String status;
    private String date;
    private String icon;
    private String color;
    private String image;

    public ObjectOrderTrackingReponse() {}

    public ObjectOrderTrackingReponse(String status, String date, String icon, String color, String image) {
        this.status = status;
        this.date = date;
        this.icon = icon;
        this.color = color;
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return (
            "ObjectOrderTrackingReponse{" +
            "status='" +
            status +
            '\'' +
            ", date='" +
            date +
            '\'' +
            ", icon='" +
            icon +
            '\'' +
            ", color='" +
            color +
            '\'' +
            ", image='" +
            image +
            '\'' +
            '}'
        );
    }
}
