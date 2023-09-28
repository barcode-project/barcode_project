package com.example.barcode;

public class shops {
    private String name_shop;
    private static int id;


    public shops(String name_shop,int id) {
        this.name_shop =name_shop;
        this.id=id;

    }

    public static int getId() {
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
