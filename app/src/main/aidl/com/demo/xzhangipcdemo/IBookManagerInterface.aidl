// IBookManagerInterface.aidl
package com.demo.xzhangipcdemo;
import com.demo.xzhangipcdemo.Book;

// Declare any non-default types here with import statements

interface IBookManagerInterface {

            void addBook(in Book book);
            List<Book> getBooks();
}
