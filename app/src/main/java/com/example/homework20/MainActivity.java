package com.example.homework20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent("com.example.homework20.ProximitySensorActivity"));
       // startActivity(new Intent("com.example.homework20.LightSensorActivity"));
    }
}
