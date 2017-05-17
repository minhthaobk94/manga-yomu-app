package com.thaontm.mangayomu.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

import lombok.Data;

@Data
public class MangaOverview extends MangaInfo {
    private String title;
    private String imageUrl;
}
