// IOnNewBookAvailableListener.aidl
package com.demo.xzhangipcdemo;
import com.demo.xzhangipcdemo.Book;


// Declare any non-default types here with import statements

interface IOnNewBookAvailableListener {
    void onNewBookAvailable(in Book newBook);

}
