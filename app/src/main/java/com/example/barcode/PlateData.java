package com.example.barcode;

public class PlateData {
    private String plateType;
    private int quantity;
    private double length;
    private double width;

    public PlateData(String plateType, int quantity, double length, double width) {
        this.plateType = plateType;
        this.quantity = quantity;
        this.length = length;
        this.width = width;
    }

    public String getPlateType() {
        return plateType;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
}
