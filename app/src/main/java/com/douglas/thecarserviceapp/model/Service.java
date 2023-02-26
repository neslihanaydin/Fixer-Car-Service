package com.douglas.thecarserviceapp.model;

public class Service {

    private int serviceId;
    private String type;
    private double cost;
    private int providerId;

    public Service(){

    }

    public Service(int serviceId, String type, double cost, int providerId) {
        this.serviceId = serviceId;
        this.type = type;
        this.cost = cost;
        this.providerId = providerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }
}
