package com.demo.xzhangipcdemo.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.demo.xzhangipcdemo.Book;
import com.demo.xzhangipcdemo.IBookManagerInterface;
import com.demo.xzhangipcdemo.R;
import com.demo.xzhangipcdemo.server.BookManagerService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private IBookManagerInterface mBookManagerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add_book).setOnClickListener(this);
        findViewById(R.id.get_books).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBookManagerInterface = IBookManagerInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBookManagerInterface = null;
            }
        };
        this.bindService(new Intent(this, BookManagerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.add_book) {
            if (mBookManagerInterface != null) {
                try {
                    mBookManagerInterface.addBook(new Book("《Android Developer》", "xzhang"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.get_books) {
            if (mBookManagerInterface != null) {
                try {
                    List<Book> books = mBookManagerInterface.getBooks();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
