package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

public class ColourGame extends AppCompatActivity {

    String colour1;
    String colour2;
    String colour3;
    String colour4;

    Button colour1Button;
    Button colour2Button;
    Button colour3Button;
    Button colour4Button;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_game);


    }
}