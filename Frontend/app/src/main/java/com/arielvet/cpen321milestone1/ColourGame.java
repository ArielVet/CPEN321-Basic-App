package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColourGame extends AppCompatActivity {

    /* Constants */
    private final int COLOUR_TIME = 500; // time it displays the colour in milliseconds
    private final int PAUSE_TIME = 100; // time it waits between colours (aka white) in milliseconds

    /* Score Variables */
    private int score; // The score of the player
    private int highScore;

    /* Boolean Logic Controllers */
    private boolean gameOver = false;
    private boolean newHighScore = false;
    private boolean playingPattern = false;

    /* Round Dependent Variable */
    private ArrayList<Integer> colourSequence; // The colour sequence shown to the player
    private int currentIndex;  // The currentIndex we are checking in a round

    /* Defined at start Arrays */
    private int[] colours; //Array contains the colours define in colours.xml
    private String[] colourSymbol; // array contains the symbols for colours for colourblind people

    /* Non Button Elements */
    private TextView scoreText; // The text element that shows score
    private TextView highScoreText; // The text element that shows High score
    private Button canvas; // The white canvas that flashes colours

    /* Extra Objects */
    private final Handler handler = new Handler(); //handler used to time the colour flashes
    SharedPreferences gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_game);

        /* Removes any current calls */
        handler.removeCallbacksAndMessages(null);

        /* Fetch the colours and the Symbols used for Colourblind people */
        colours = new int[] {getColor(R.color.colour0), getColor(R.color.colour1), getColor(R.color.colour2), getColor(R.color.colour3)};
        colourSymbol = new String[] {getString(R.string.colour0_cap), getString(R.string.colour1_cap), getString(R.string.colour2_cap), getString(R.string.colour3_cap)};

        /* Non Button Elements */
        canvas = findViewById(R.id.canvas);
        scoreText = findViewById(R.id.score_text);
        highScoreText = findViewById(R.id.high_score_text);

        /* Game Data*/
        gameData = getSharedPreferences("CPEN321_data", Context.MODE_PRIVATE);
        highScore = gameData.getInt("high_score", 0);
        updateHighScore();

        /* Colour Button Deceleration */
        declareButton(R.id.colour0_button,0);
        declareButton(R.id.colour1_button,1);
        declareButton(R.id.colour2_button,2);
        declareButton(R.id.colour3_button,3);

        findViewById(R.id.reset_score_button).setOnClickListener(view -> {
            highScore = 0;
            SharedPreferences.Editor editor = gameData.edit();
            editor.putInt("high_score", 0);
            editor.apply();
            updateHighScore();
        });

        // Launch New Game when someone presses the button
        findViewById(R.id.new_game_button).setOnClickListener(view -> playGame());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * Purpose: To declare the colour buttons and their event handlers
     *
     * @param id : the id of buttons
     * @param colourIndex : the associated number with the button. button with
     *                      id colour0_button has index 0
     * */
    @SuppressLint("ClickableViewAccessibility")
    private void declareButton(int id, int colourIndex){
        // Identify the button and add listeners for when it is pressed and when it is released
        findViewById(id).setOnTouchListener((view, motionEvent) -> {

            // If game is over Display a message that they must start a new game
            if (gameOver){
                Toast.makeText(this, "Game Over! Press NEW GAME to start a New Game!", Toast.LENGTH_SHORT).show();
            }
            else if (playingPattern){
                Toast.makeText(this, "Wait For Pattern to Finish Playing", Toast.LENGTH_SHORT).show();
            }
            // Else trigger button Presses
            else {
                // When the button is pressed, update its colour and symbol
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    canvas.setBackgroundTintList(ColorStateList.valueOf(colours[colourIndex]));
                    canvas.setText(colourSymbol[colourIndex]);
                }
                // When it is released, update to white, no symbol, and run verifySequence
                // to ensure its the right press
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    canvas.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
                    canvas.setText("");
                    verifySequence(colourIndex);
                }
            }
            return false;
        });
    }

    /**
     * Purpose: The function resets the score and colourSequence variables
     *          and begins a new round
     */
    private void playGame(){

        // Resets the variables
        gameOver = false;
        newHighScore = false;
        score = 0;
        updateScore();

        // Reset the colourSequence
        colourSequence = new ArrayList<>();

        playRound();
    }

    /**
     * Purpose: Function begins new round:
     *          1.  First the function resets the currentIndex element (this element is used
     *              by the program to detect the current colour in the sequence the user is
     *              supposed to be inputting)
     *          2.  The Function Launches playColourSequence to display the current sequence
     *              of colours.
     *          3.  Once the colours have been played, we wait for an asynchronous event to
     *              occur. This event will be triggered by verifySequence.
     *
     */
    private void playRound(){

        //Control Variables
        currentIndex = 0;

        // Adds another colour to the sequence
        colourSequence.add((int) (Math.random() * 4));

        // Play the Colours
        playColourSequence();
    }

    /**
     * Purpose: Function will play the colours currently in a list with delays. Instead of using
     *          infinite loops or thread.sleeps which pause program. This uses a handler to schedule
     *          colour changes accordingly.
     */
    private void playColourSequence(){
        playingPattern = true;
        for (int i = 0; i < colourSequence.size(); i++){

            //Get next colour from list
            int displayColour = colours[colourSequence.get(i)];
            String colourSymbol = this.colourSymbol[colourSequence.get(i)];

            // Logic element that detects when last patern occurs
            Boolean lastRound = (i == colourSequence.size() - 1);

            /*  Delay Handler that will display the new colour every (COLOUR TIME + PAUSE TIME) units
                Canvas will be displayColour for COLOUR time units, and THEN
                Canvas will be white  for PAUSE time units before proceeding to next colour
             */
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

                            // On the last pattern set playing to false
                            if (lastRound) playingPattern = false;

                        }
                    }, COLOUR_TIME);
                }
            }, (long) i * (COLOUR_TIME + PAUSE_TIME));
        }
    }

    /**
     * Purpose: Function checks the asynchronous events of this game.
     *          When a button is pressed, it calls this function with its associated colour
     *          The function then checks if this colour is the next colour in the sequence
     *          If we reached the end of the sequence successfully, start a new round,
     *          else display game over
     *
     * @param pressedColour : The colour currently pressed by the user
     *
     */
    private void verifySequence(int pressedColour){
        if (colourSequence.get(currentIndex) == pressedColour){
            currentIndex++;

            //Reached end Successfully
            if (currentIndex == colourSequence.size()){
                score++;
                if (score > highScore){
                    highScore = score;
                    SharedPreferences.Editor editor = gameData.edit();
                    editor.putInt("high_score", highScore);
                    editor.apply();
                    updateHighScore();
                    newHighScore = true;
                }
                updateScore();
                playRound();
            }
        }
        else{
            gameOver = true;
            handler.removeCallbacksAndMessages(null);
            new AlertDialog.Builder(this)
                    .setTitle("Game Over!")
                    .setMessage( ((newHighScore) ? " New High Score: ": "Your Final Score is: ") + score)
                    .setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("New Game", (dialogInterface, i) -> playGame())
                    .create()
                    .show();
        }

    }

    /**
     * Purpose: Updates the score on screen to show the score Element
     * */
    @SuppressLint("SetTextI18n")
    private void updateScore(){
        scoreText.setText(getString(R.string.score_cap) + " " + score);
    }

    /**
     * Purpose: Updates the High score on screen to show the score Element
     * */
    @SuppressLint("SetTextI18n")
    private void updateHighScore(){
        highScoreText.setText(getString(R.string.high_cap) + " " + highScore);
    }


}