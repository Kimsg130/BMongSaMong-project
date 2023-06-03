package com.inhatc.bmongsamong_project;

public class Item { //리스트뷰의 아이템을 담을 클래스
    private String title;
    private String date;

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

    public Item(String title, String date) {
        this.title = title;
        this.date = date;
    }
}
