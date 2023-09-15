package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ColourGame extends AppCompatActivity {

    // Constants
    private final int COLOUR_TIME = 500; // in milliseconds
    private final int PAUSE_TIME = 100;

    // Global Variables
    private int score;
    private int currentIndexCheck;


    private ArrayList<Integer> colourSequence;
    private int[] colours;
    private String[] colours_cap;

    //TODO MAYBE ADD HIGH SCORE

    // Non Button Elements
    private TextView scoreText;
    private Button canvas;

    // Handler to add timers
    private final Handler handler = new Handler();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_game);

        // Removes any current calls
        handler.removeCallbacksAndMessages(null);

        /* Fetch the colours and the Symbols used for Colourblind people */
        colours = new int[] {getColor(R.color.colour1), getColor(R.color.colour2), getColor(R.color.colour3), getColor(R.color.colour4)};
        colours_cap = new String[] {getString(R.string.colour1_cap), getString(R.string.colour2_cap), getString(R.string.colour3_cap), getString(R.string.colour4_cap)};

        /* Non Button Elements */
        canvas = findViewById(R.id.canvas);
        scoreText = findViewById(R.id.score_text);

        /* Buttons */
        Button colour1Button = findViewById(R.id.colour1_button);
        colour1Button.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                canvas.setBackgroundTintList(ColorStateList.valueOf(colours[0]));
                canvas.setText(colours_cap[0]);
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                canvas.setText("");
            }
            return false;
        });

        Button colour2Button = findViewById(R.id.colour2_button);
        colour2Button.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                canvas.setBackgroundTintList(ColorStateList.valueOf(colours[1]));
                canvas.setText(colours_cap[1]);
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                canvas.setText("");
            }
            return false;
        });

        Button colour3Button = findViewById(R.id.colour3_button);
        colour3Button.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                canvas.setBackgroundTintList(ColorStateList.valueOf(colours[2]));
                canvas.setText(colours_cap[2]);
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                canvas.setText("");
            }
            return false;
        });

        Button colour4Button = findViewById(R.id.colour4_button);
        colour4Button.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                canvas.setBackgroundTintList(ColorStateList.valueOf(colours[3]));
                canvas.setText(colours_cap[3]);
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                canvas.setText("");
            }
            return false;
        });

        // Button Elements
        Button newGameButton = findViewById(R.id.new_game_button);
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

        // Reset the colourSequence
        colourSequence = new ArrayList<>();

        // Keep playing rounds until you fail a round
        while (playRound()){
            // If you pass the round successfully, update the score and move on to round
            score++;
            updateScore();
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateScore(){
        scoreText.setText(getString(R.string.score_cap) + " " + score);
    }

    private boolean playRound(){

        //Control Variables
        currentIndexCheck = 0;


        // Adds another colour to the sequence
        colourSequence.add((int) (Math.random() * 4));

        // Play the Colours
        playColourSequence();

        // Listen to the Colours
        while (currentIndexCheck < colourSequence.size()){

            currentIndexCheck++;
        }


        return false;
    }

    /**
     * Purpose: Function will play the colours currently in a list with delays
     * */
    private void playColourSequence(){
        for (int i = 0; i < colourSequence.size(); i++){

            //Get next colour from list
            int displayColour = colours[colourSequence.get(i)];
            String colourSymbol = colours_cap[colourSequence.get(i)];

            // Delay Handler that will display the new colour every (COLOUR TIME + PAUSE TIME) units
            // Canvas will be displayColour for COLOUR time units
            // Canvas will be white  for PAUSE time units before proceeding to next colour
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Updates the colour to the current one in list
                    canvas.setBackgroundTintList(ColorStateList.valueOf(displayColour));
                    canvas.setText(colourSymbol);

                    // Update the canvas to white in COLOUR TIME units
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                            canvas.setText("");
                        }
                    }, COLOUR_TIME);
                }
            }, (long) i * (COLOUR_TIME + PAUSE_TIME));
        }
    }

    /**
     * Purpose: Triggered by button press and checks if
     *          colour pressed = colour[index]
     *
     */

    private boolean checkColour(int index, int currentColour){
        return false;
    }




}