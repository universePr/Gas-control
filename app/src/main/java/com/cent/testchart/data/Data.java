package com.cent.testchart.data;

public class Data {
    private int count = 0;
    private String time;
    private String tag;

    public Data(int count, String time, String tag) {
        this.count = count;
        this.time = time;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
