package com.waydrow.litepaltest;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector.getDatabase();
            }
        });

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book("Dan Brown", 16.96, 454, "The Da Vinci Code", "Unknow");
                book.save();
            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
//                book.setPrice(14.95);
//                book.setPress("Anchor");
//                book.updateAll("name = ? and author = ?", "The Da Vinci Code", "Dan Brown");
                book.setToDefault("pages");
                book.updateAll();

                // 删除数据
//                DataSupport.deleteAll(Book.class, "price < ?", "15");
            }
        });

        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> books = DataSupport.findAll(Book.class);
                for (Book book : books) {
                    Log.d(TAG, "book name is " + book.getName());
                    Log.d(TAG, "book author is " + book.getAuthor());
                    Log.d(TAG, "book price is " + book.getPrice());
                }
            }
        });
    }
}
