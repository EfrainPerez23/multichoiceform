package com.hypernovalabs.multichoiceform.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lucascabrales on 1/8/18.
 */

public class ExtraModel implements Parcelable {
    public ArrayList<String> data;
    public String selection;
    public String title;
    public int id;
    public boolean required;
    public int toolbarBackgroundColor;
    public int toolbarTitleColor;

    public ExtraModel() {

    }

    protected ExtraModel(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<String>();
            in.readList(data, String.class.getClassLoader());
        } else {
            data = null;
        }
        selection = in.readString();
        title = in.readString();
        id = in.readInt();
        required = in.readByte() != 0x00;
        toolbarBackgroundColor = in.readInt();
        toolbarTitleColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (data == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(data);
        }
        dest.writeString(selection);
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeByte((byte) (required ? 0x01 : 0x00));
        dest.writeInt(toolbarBackgroundColor);
        dest.writeInt(toolbarTitleColor);
    }

    @SuppressWarnings("unused")
    public static final Creator<ExtraModel> CREATOR = new Creator<ExtraModel>() {
        @Override
        public ExtraModel createFromParcel(Parcel in) {
            return new ExtraModel(in);
        }

        @Override
        public ExtraModel[] newArray(int size) {
            return new ExtraModel[size];
        }
    };
}
