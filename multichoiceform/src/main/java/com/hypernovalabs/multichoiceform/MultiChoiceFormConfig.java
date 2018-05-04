package com.hypernovalabs.multichoiceform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Main configuration model.
 * Defines everything that needs to be sent to OptionsActivity. Only to be used internally.
 */
public class MultiChoiceFormConfig implements Parcelable {

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
    /**
     * Key to the view ID parameter.
     */
    public static final String EXTRA_ID_KEY = EXTRA_PREFIX + "IdKey";

    public ArrayList<String> data;
    public String selection;
    public String title;
    public int id;
    public boolean required;
    public int toolbarBackgroundColor;
    public int toolbarTitleColor;
    public String emptyViewTitle;
    public String emptyViewMsg;
    public boolean isSearchable;

    public MultiChoiceFormConfig() {

    }

    //region Parcelable
    protected MultiChoiceFormConfig(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<>();
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
        emptyViewTitle = in.readString();
        emptyViewMsg = in.readString();
        isSearchable = in.readByte() != 0x00;
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
        dest.writeString(emptyViewTitle);
        dest.writeString(emptyViewMsg);
        dest.writeByte((byte) (isSearchable ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<MultiChoiceFormConfig> CREATOR = new Creator<MultiChoiceFormConfig>() {
        @Override
        public MultiChoiceFormConfig createFromParcel(Parcel in) {
            return new MultiChoiceFormConfig(in);
        }

        @Override
        public MultiChoiceFormConfig[] newArray(int size) {
            return new MultiChoiceFormConfig[size];
        }
    };
    //endregion
}
