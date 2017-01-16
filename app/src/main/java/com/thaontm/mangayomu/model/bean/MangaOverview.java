package com.thaontm.mangayomu.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

import lombok.Data;

@Data
public class MangaOverview implements Parcelable {
    private int id;
    private String previewImageUrl;
    private String name;
    private List<String> genres;
    private String mDescription;
    private String mState;

    public MangaOverview() {

    }

    protected MangaOverview(Parcel in) {
        id = in.readInt();
        previewImageUrl = in.readString();
        name = in.readString();
        genres = in.createStringArrayList();
        mDescription = in.readString();
        mState = in.readString();
    }

    public static final Creator<MangaOverview> CREATOR = new Creator<MangaOverview>() {
        @Override
        public MangaOverview createFromParcel(Parcel in) {
            return new MangaOverview(in);
        }

        @Override
        public MangaOverview[] newArray(int size) {
            return new MangaOverview[size];
        }
    };

    public String getGenresAsString() {
        return TextUtils.join(", ", genres);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(previewImageUrl);
        dest.writeString(name);
        dest.writeStringList(genres);
        dest.writeString(mDescription);
        dest.writeString(mState);
    }
}
