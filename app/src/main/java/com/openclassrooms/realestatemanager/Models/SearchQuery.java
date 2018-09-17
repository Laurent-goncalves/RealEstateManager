package com.openclassrooms.realestatemanager.Models;

public class SearchQuery {

    private Boolean soldStatus;
    private String typeProperty;
    private String datePublishStart;
    private String datePublishEnd;
    private Double priceInf;
    private Double priceSup;
    private Double surfaceInf;
    private Double surfaceSup;
    private int roomNbMin;
    private Double searchLocLat;
    private Double searchLocLng;
    private String address;
    private int radius;

    public SearchQuery() {
    }

    public SearchQuery(Boolean soldStatus, String typeProperty, String datePublishStart, String datePublishEnd, Double priceInf, Double priceSup, Double surfaceInf, Double surfaceSup, int roomNbMin, Double searchLocLat, Double searchLocLng, String address, int radius) {
        this.soldStatus = soldStatus;
        this.typeProperty = typeProperty;
        this.datePublishStart = datePublishStart;
        this.datePublishEnd = datePublishEnd;
        this.priceInf = priceInf;
        this.priceSup = priceSup;
        this.surfaceInf = surfaceInf;
        this.surfaceSup = surfaceSup;
        this.roomNbMin = roomNbMin;
        this.searchLocLat = searchLocLat;
        this.searchLocLng = searchLocLng;
        this.address = address;
        this.radius = radius;
    }

    public Boolean getSoldStatus() {
        return soldStatus;
    }

    public void setSoldStatus(Boolean soldStatus) {
        this.soldStatus = soldStatus;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
    }

    public String getDatePublishStart() {
        return datePublishStart;
    }

    public void setDatePublishStart(String datePublishStart) {
        this.datePublishStart = datePublishStart;
    }

    public String getDatePublishEnd() {
        return datePublishEnd;
    }

    public void setDatePublishEnd(String datePublishEnd) {
        this.datePublishEnd = datePublishEnd;
    }

    public Double getPriceInf() {
        return priceInf;
    }

    public void setPriceInf(Double priceInf) {
        this.priceInf = priceInf;
    }

    public Double getPriceSup() {
        return priceSup;
    }

    public void setPriceSup(Double priceSup) {
        this.priceSup = priceSup;
    }

    public Double getSurfaceInf() {
        return surfaceInf;
    }

    public void setSurfaceInf(Double surfaceInf) {
        this.surfaceInf = surfaceInf;
    }

    public Double getSurfaceSup() {
        return surfaceSup;
    }

    public void setSurfaceSup(Double surfaceSup) {
        this.surfaceSup = surfaceSup;
    }

    public int getRoomNbMin() {
        return roomNbMin;
    }

    public void setRoomNbMin(int roomNbMin) {
        this.roomNbMin = roomNbMin;
    }

    public Double getSearchLocLat() {
        return searchLocLat;
    }

    public void setSearchLocLat(Double searchLocLat) {
        this.searchLocLat = searchLocLat;
    }

    public Double getSearchLocLng() {
        return searchLocLng;
    }

    public void setSearchLocLng(Double searchLocLng) {
        this.searchLocLng = searchLocLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
