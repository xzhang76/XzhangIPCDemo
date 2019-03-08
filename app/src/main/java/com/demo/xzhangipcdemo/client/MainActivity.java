package com.demo.xzhangipcdemo.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.demo.xzhangipcdemo.Book;
import com.demo.xzhangipcdemo.IBookManagerInterface;
import com.demo.xzhangipcdemo.R;
import com.demo.xzhangipcdemo.server.BookManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IBookManagerInterface mBookManagerInterface;
    private BookRecyclerViewAdapter mAdapter;
    private List<Book> mBooks = new ArrayList<>();
    private IBinder.DeathRecipient mDeathRecipient; //Binder连接断裂监听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add_book).setOnClickListener(this);
        findViewById(R.id.get_books).setOnClickListener(this);

        mAdapter = new BookRecyclerViewAdapter(mBooks);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_books);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                //Binder连接断裂时回调
                if (mBookManagerInterface == null) {
                    return;
                }
                mBookManagerInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
                mBookManagerInterface = null;
                bindRemoteService();

            }
        };
        bindRemoteService();
    }

    private void bindRemoteService() {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBookManagerInterface = IBookManagerInterface.Stub.asInterface(service);
                //给Binder设置死亡代理，Binder连接断裂时会回调其binderDied()
                try {
                    service.linkToDeath(mDeathRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
                    mBookManagerInterface.addBook(new Book("《Android Developer》——" + new Random().nextInt(100), "xzhang"));
                    Toast.makeText(this, "Add successfully!", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (id == R.id.get_books) {
            if (mBookManagerInterface != null) {
                try {
                    mBooks = mBookManagerInterface.getBooks();
                    mAdapter.updateBooks(mBooks);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
