package com.demo.xzhangipcdemo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.demo.xzhangipcdemo.Book;
import com.demo.xzhangipcdemo.IBookManagerInterface;
import com.demo.xzhangipcdemo.IOnNewBookAvailableListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    private List<Book> mBooks;
    private RemoteCallbackList<IOnNewBookAvailableListener> mListenerList = new RemoteCallbackList<>();
    private AtomicBoolean mIsServiceDestoryed;

    @Override
    public void onCreate() {
        super.onCreate();
        if (null == mBooks) {
            mBooks = new ArrayList<>();
        }
        mIsServiceDestoryed = new AtomicBoolean(false);
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private IBinder mIBinder = new IBookManagerInterface.Stub() {

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

        @Override
        public void registerListener(IOnNewBookAvailableListener listener) {
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookAvailableListener listener) {
            mListenerList.unregister(listener);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }
}
