// IMyAidlInterface.aidl
package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

// Declare any non-default types here with import statements
import  com.example.mivanzhang.collapsingtoolbarlayoutdemo.Book;
interface IMyAidlInterface {
            Book getBook();
             Book addBook(in Book book);
              Book addBookOut(out Book book);
              Book addBookInOut(inout Book book);
}
