package com.demo.xzhangipcdemo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.demo.xzhangipcdemo.Book;
import com.demo.xzhangipcdemo.IBookManagerInterface;

import java.util.ArrayList;
import java.util.List;

public class BookManagerService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        if (null == mBooks) {
            mBooks = new ArrayList<>();
        }
    }

    private List<Book> mBooks;

    private IBinder mIBinder = new IBookManagerInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (null != mBooks) {
                mBooks.add(book);
            }
        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            return mBooks;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }
}
