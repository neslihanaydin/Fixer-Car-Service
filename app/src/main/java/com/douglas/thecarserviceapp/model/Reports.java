package com.douglas.thecarserviceapp.model;

public class Reports {

    private int reportId;
    private int appointmentId;

    public  Reports(){

    }
    public Reports(int reportId, int appointmentId) {
        this.reportId = reportId;
        this.appointmentId = appointmentId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
}
