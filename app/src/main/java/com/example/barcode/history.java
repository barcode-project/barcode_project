package com.example.barcode;

public class history {
    private String time;
    private String history_name;
    private String id_nu;

    public history(String time, String history_name, String id_nu) {
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

    public void setHistory_name(String history_name) {
        this.history_name = history_name;
    }

    public String getId_nu() {
        return id_nu;
    }

    public void setId_nu(String id_nu) {
        this.id_nu = id_nu;
    }
}
