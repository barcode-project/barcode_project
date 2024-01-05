package com.example.barcode.Items;

public class history {
    private String time;
    private String history_name;
    private static int id_nu;

    public history(String time, String history_name, int id_nu) {
        this.time = time;
        this.history_name = history_name;
        this.id_nu = id_nu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHistory_name() {
        return history_name;
    }


    public static int getId_nu() {
        return id_nu;
    }

    public void setId_nu(int id_nu) {
        this.id_nu = id_nu;
    }
}
