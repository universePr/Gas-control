package com.cent.testchart.data;

public class Data {
    private int count;
    private String time;

    public Data(int count, String time) {
        this.count = count;
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
