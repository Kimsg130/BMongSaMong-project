package com.inhatc.bmongsamong_project;

public class Item { //리스트뷰의 아이템을 담을 클래스
    private String title;
    private String date;

    private int d_id;

    public int getD_id() {
        return d_id;
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Item(String title, String date, int d_id) {
        this.title = title;
        this.date = date;
        this.d_id = d_id;
    }
}
