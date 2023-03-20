package com.douglas.thecarserviceapp.util;

public class ItemDrawer {
    private String txt;
    private int imgId;

    public ItemDrawer(int imgId, String txt) {
        this.txt = txt;
        this.imgId = imgId;
    }

    public String getTxt() {
        return txt;
    }

    public int getImgId() {
        return imgId;
    }
}