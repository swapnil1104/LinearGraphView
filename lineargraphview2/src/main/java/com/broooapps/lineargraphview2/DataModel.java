package com.broooapps.lineargraphview2;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class DataModel {

    int data;

    @ColorInt
    int colorInt;

    String colorRes;

    @Nullable
    String title;

    public DataModel(String title, int colorInt, int data) {
        this.data = data;
        this.title = title;
        this.colorInt = colorInt;
    }

    public DataModel(String title, String colorRes, int data) {
        this.data = data;
        this.title = title;
        this.colorRes = colorRes;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getColorRes() {
        return colorRes;
    }

    public int getColorInt() {
        return colorInt;
    }
}
