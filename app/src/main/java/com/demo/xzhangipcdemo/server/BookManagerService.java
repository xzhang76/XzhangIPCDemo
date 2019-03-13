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
        new Thread(new ServiceWorker()).start();
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

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookIndex = 1;
                if (mBooks != null) {
                    bookIndex = mBooks.size() + 1;
                }
                Book newBook = new Book("《New Android Developer " + bookIndex +"》", "xzhang");
                //通知client端
                onNewBookAvailable(newBook);
            }
        }
    }

    private void onNewBookAvailable(Book book) {
        if (mBooks == null) {
            mBooks = new ArrayList<>();
        }
        mBooks.add(book);
        int listenerListSize = mListenerList.beginBroadcast();
        for (int i = 0; i < listenerListSize; i++) {
            IOnNewBookAvailableListener listener = mListenerList.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.onNewBookAvailable(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }
}
