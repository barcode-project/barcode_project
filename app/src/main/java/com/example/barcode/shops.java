package com.example.barcode;

public class shops {
    private String name_shop;
    private String owner_name;
    private String status;

    private int id;

    public shops() {

    }

    public shops(String name_shop, String owner_name, String status, int id) {
        this.name_shop = name_shop;
        this.owner_name = owner_name;
        this.status = status;
        this.id = id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_shop() {
        return name_shop;
    }

    public void setName_shop(String name_shop) {
        this.name_shop = name_shop;
    }
}
