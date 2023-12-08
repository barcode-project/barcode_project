package com.example.barcode;

public class PlateData {
    private String plateType;
    private String quantity;
    private String length;
    private String width;

    public PlateData(String plateType, String quantity, String length, String width) {
        this.plateType = plateType;
        this.quantity = quantity;
        this.length = length;
        this.width = width;
    }

    public PlateData() {
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
