package com.douglas.thecarserviceapp.model;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Appointment implements Serializable {

    private int appointmentId;
    private int userId;
    private int providerId;
    private int serviceId;
    Date date;
    Time time;

    public Appointment(){

    }

    public Appointment(int appointmentId, int userId, int providerId, int serviceId, Date date, Time time) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.date = date;
        this.time = time;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
