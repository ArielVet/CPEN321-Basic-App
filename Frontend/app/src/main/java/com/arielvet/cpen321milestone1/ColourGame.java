package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ColourGame extends AppCompatActivity {

    private final int COLOUR_TIME = 500; // in milliseconds
    private final int PAUSE_TIME = 100;


    //TODO MAYBE ADD HIGH SCORE
    private int score;

    private ArrayList<Integer> colourSequence;
    private int[] colours;
    private String[] colours_cap;
    private final Handler handler = new Handler();

    private TextView scoreText;

    private Button canvas;

    private Button newGameButton;
    private Button colour1Button;
    private Button colour2Button;
    private Button colour3Button;
    private Button colour4Button;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_game);


        /* Fetch the colours and the Symbols used for Colourblind people */
        colourSequence = new ArrayList<>();
        colours = new int[] {getColor(R.color.colour1), getColor(R.color.colour2), getColor(R.color.colour3), getColor(R.color.colour4)};
        colours_cap = new String[] {getString(R.string.colour1_cap), getString(R.string.colour2_cap), getString(R.string.colour3_cap), getString(R.string.colour4_cap)};

        /* Fetch the buttons */
        canvas = findViewById(R.id.canvas);


        newGameButton = findViewById(R.id.new_game_button);
        colour1Button = findViewById(R.id.colour1_button);
        colour2Button = findViewById(R.id.colour2_button);
        colour3Button = findViewById(R.id.colour3_button);
        colour4Button = findViewById(R.id.colour4_button);

        scoreText = findViewById(R.id.score_text);

        /* Play the Game*/
        newGameButton.setOnClickListener(view -> playGame());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null);
    }

    private void playGame(){

        // Reset the Score
        score = 0;
        updateScore();

        while (playRound()){
            score++;
            updateScore();
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateScore(){
        scoreText.setText(getString(R.string.score_cap) + " " + score);
    }

    private boolean playRound(){

        // Adds another colour to the sequence
        colourSequence.add((int) (Math.random() * 4));

        // Play the Colours
        playColours();

        // Listen to the Colours

        return false;
    }

    /**
     * Purpose: Function will play the colours currently in a list with delays
     * */
    private void playColours(){
        for (int i = 0; i < colourSequence.size(); i++){

            //Get next colour from list
            int displayColour = colours[colourSequence.get(i)];

            // Delay Handler that will display the new colour every (COLOUR TIME + PAUSE TIME) units
            // Canvas will be displayColour for COLOUR time units
            // Canvas will be white  for PAUSE time units before proceeding to next colour
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Updates the colour to the current one in list
                    canvas.setBackgroundTintList(ColorStateList.valueOf(displayColour));

                    // Update the canvas to white in COLOUR TIME units
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                        }
                    }, COLOUR_TIME);
                }
            }, (long) i * (COLOUR_TIME + PAUSE_TIME));
        }
    }





}