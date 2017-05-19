package com.thaontm.mangayomu.model.bean;

import android.graphics.Rect;

import lombok.Data;

@Data
public class TouchInfo {
    private float x1, y1, x2, y2;

    public TouchInfo(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Rect getRect() {
        int top, left, right, bottom;
        top = (int) (y1 < y2 ? y1 : y2);
        bottom = (int) (y1 >= y2 ? y1 : y2);
        left = (int) (x1 < x2 ? x1 : x2);
        right = (int) (x1 >= x2 ? x1 : x2);
        return new Rect(left, top, right, bottom);
    }
}
