// IBookManagerInterface.aidl
package com.demo.xzhangipcdemo;
import com.demo.xzhangipcdemo.Book;
import com.demo.xzhangipcdemo.IOnNewBookAvailableListener;

// Declare any non-default types here with import statements

interface IBookManagerInterface {

            void addBook(in Book book);
            List<Book> getBooks();
            void registerListener(IOnNewBookAvailableListener listener);
            void unregisterListener(IOnNewBookAvailableListener listener);

}
