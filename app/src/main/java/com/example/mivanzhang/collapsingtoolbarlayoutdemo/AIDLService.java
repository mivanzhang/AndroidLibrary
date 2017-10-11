package com.example.mivanzhang.collapsingtoolbarlayoutdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmeng on 2017/10/11.
 */

public class AIDLService extends Service {
    public final String TAG = this.getClass().getSimpleName();

    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    //由AIDL文件生成的BookManager
    private final IMyAidlInterface.Stub mIShop = new IMyAidlInterface.Stub() {


        @Override
        public Book getBook() throws RemoteException {
            Log.e(TAG, "parameter in getBook Book is ");
            return new Book("getBook", 0);
        }

        @Override
        public Book addBook(Book book) throws RemoteException {
            Log.e(TAG, "parameter in addBook Book is " + book.toString() + "  name " + book.getName());
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if (book == null) {
                    Log.e(TAG, "Book is null in In");
                    book = new Book();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.d(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
            }
            return new Book("addBook", 30);
        }

        @Override
        public Book addBookOut(Book book) throws RemoteException {
            Log.e(TAG, "parameter in addBookOut Book is " + book.toString() + "  name " + book.getName());
            book.setName("addBookOut");
            return new Book("addBookOut", 40);
        }

        @Override
        public Book addBookInOut(Book book) throws RemoteException {
            Log.e(TAG, "parameter in addBookInOut Book is " + book.toString() + "  name " + book.getName());
            return new Book("addBookInOut", 90);
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android");
        book.setPrice(10);
        mBooks.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIShop;
    }
}
