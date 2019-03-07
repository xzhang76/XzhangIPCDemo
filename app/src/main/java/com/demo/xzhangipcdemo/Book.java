package com.demo.xzhangipcdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public String mBookName = "";
    public String mBookAuthor = "";

    protected Book(Parcel in) {
        mBookName = in.readString();
        mBookAuthor = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBookName);
        dest.writeString(mBookAuthor);
    }
}
