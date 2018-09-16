package com.waydrow.filepersistencetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SharedActivity extends AppCompatActivity {
    private static final String TAG = "SharedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        Button saveData = (Button) findViewById(R.id.save_data);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("name", "Waydrow");
                editor.putInt("age", 22);
                editor.putBoolean("married", false);
                editor.apply();
            }
        });

        Button restoreData = (Button) findViewById(R.id.restore_data);
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
                String name = preferences.getString("name", "");
                int age = preferences.getInt("age", 0);
                boolean married = preferences.getBoolean("married", false);
                Log.d(TAG, "name is " + name);
                Log.d(TAG, "age is " + age);
                Log.d(TAG, "married is " + married);
            }
        });
    }
}
