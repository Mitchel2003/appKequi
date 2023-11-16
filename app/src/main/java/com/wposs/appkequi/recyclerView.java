package com.wposs.appkequi;

public class recyclerView {

    private String color;
    private String name;
    private String numberPhone;
    private String cash;
    private String message;
    private String status;

    public recyclerView(String color, String name, String numberPhone, String cash, String message, String status) {
        this.color = color;
        this.name = name;
        this.numberPhone = numberPhone;
        this.cash = cash;
        this.message = message;
        this.status = status;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPhone() {
        return numberPhone;
    }
    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getCash() {
        return cash;
    }
    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
