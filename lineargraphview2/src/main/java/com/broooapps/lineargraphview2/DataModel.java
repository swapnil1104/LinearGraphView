package com.broooapps.lineargraphview2;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class DataModel {

    int value;

    @ColorInt
    int colorInt;

    String colorRes;

    @Nullable
    String title;

    public DataModel(String title, int colorInt, int value) {
        this.value = value;
        this.title = title;
        this.colorInt = colorInt;
    }

    public DataModel(String title, String colorRes, int value) {
        this.value = value;
        this.title = title;
        this.colorRes = colorRes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getColorRes() {
        return colorRes;
    }

    public int getColorInt() {
        return colorInt;
    }
}
