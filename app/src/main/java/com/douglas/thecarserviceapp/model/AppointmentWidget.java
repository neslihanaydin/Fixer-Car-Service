package com.douglas.thecarserviceapp.model;

public class AppointmentWidget {
    Integer wImg;
    String time;
    String customerName;
    String dropOrPick;
    Integer btnDetail;

    public AppointmentWidget(){

    }

    public AppointmentWidget(Integer wImg, String time, String customerName, String dropOrPick, Integer btnDetail) {
        this.wImg = wImg;
        this.time = time;
        this.customerName = customerName;
        this.dropOrPick = dropOrPick;
        this.btnDetail = btnDetail;
    }

    public Integer getwImg() {
        return wImg;
    }

    public void setwImg(Integer wImg) {
        this.wImg = wImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDropOrPick() {
        return dropOrPick;
    }

    public void setDropOrPick(String dropOrPick) {
        this.dropOrPick = dropOrPick;
    }

    public Integer getBtnDetail() {
        return btnDetail;
    }

    public void setBtnDetail(Integer btnDetail) {
        this.btnDetail = btnDetail;
    }
}
