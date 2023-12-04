package com.example.barcode;

public class ListItem {
    private String name;
    private String age;
    private String sex;

    public ListItem(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getAgeSex() {
        return age + " years, " + sex;
    }
}

