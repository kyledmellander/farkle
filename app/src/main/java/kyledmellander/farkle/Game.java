package kyledmellander.farkle;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {
    private static final String TAG = Game.class.getSimpleName();
    private int numPlayers;
    private static Toast toast;
    private DisplayMetrics metrics;
    private Die[] dice = new Die[6];
    private ScoreManager scoreManager;
    private TextView playerTurnView;
    private TextView scoreView;
    private TextView roundScoreView;
    private TextView topScoreView;
    private MediaPlayer mediaPlayer;

    private int checkSelection() {
        ArrayList<Die> selectedDice = new ArrayList<>();
        for (Die die : dice) {
            if (die.getStatus() == Die.Status.selected) {
               selectedDice.add(die);
            }
        }

        int score = scoreManager.calculateScore(selectedDice);
        return score;
    }

    private int numDiceSelected() {
        int numSelected = 0;
        for (Die die : dice) {
            if (die.getStatus() == Die.Status.selected) {
                numSelected += 1;
            }
        }
        return numSelected;
    }

    private int addSelection() {
        ArrayList<Die> selectedDice = new ArrayList<>();
        for (Die die : dice) {
            if (die.getStatus() == Die.Status.selected) {
               selectedDice.add(die);
            }
        }
        int score = scoreManager.addSelected(selectedDice);
        return score;
    }

    private void initDiceImages() {
        ImageView die1 = (ImageView) findViewById(R.id.die1);
        ImageView die2 = (ImageView) findViewById(R.id.die2);
        ImageView die3 = (ImageView) findViewById(R.id.die3);
        ImageView die4 = (ImageView) findViewById(R.id.die4);
        ImageView die5 = (ImageView) findViewById(R.id.die5);
        ImageView die6 = (ImageView) findViewById(R.id.die6);

        ArrayList<ImageView> diceImages = new ArrayList<>(6);
        diceImages.add(die1);
        diceImages.add(die2);
        diceImages.add(die3);
        diceImages.add(die4);
        diceImages.add(die5);
        diceImages.add(die6);

        for (int i = 0; i < diceImages.size(); i++) {
            dice[i] = new Die(diceImages.get(i),metrics);
        }
    }

    // Generally return true. If the end of the turn button has been pressed,
    // Check to see that the selected dice are valid.
    private boolean rollDice(boolean endTurn) {
        int selectedScore = addSelection();
        boolean moreDice = false;

        for (int i = 0; i < dice.length; i++) {
            if (dice[i].getStatus() == Die.Status.ready) {
                moreDice = true;
            }
        }

        if (endTurn && numDiceSelected() > 0) {
            if (checkSelection() > 0) {

            }
        }
        if (selectedScore > 0 ||
                (endTurn && numDiceSelected() > 0 && checkSelection() > 0) ||
                (endTurn && numDiceSelected() == 0)) {
            for (int i = 0; i < dice.length; i++) {
                dice[i].rollDie(metrics, !moreDice || endTurn);
            }
            updateView();
            return true;
        }
        else {
            return false;
        }
    }

    private void checkFarkle() {
        ArrayList<Die> remainingDice = new ArrayList<>();
        for (Die d : dice) {
            if (d.getStatus()==Die.Status.ready) {
                remainingDice.add(d);
            }
        }
        if (scoreManager.checkFarkle(remainingDice)) {
            roundScoreView.setText("Round: FARKLE");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            numPlayers = extras.getInt("numPlayers");
            scoreManager = ScoreManager.getScoreManager(numPlayers);
        }
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        initDiceImages();

        mediaPlayer = MediaPlayer.create(getBaseContext(),R.raw.dicesound);

        Button b = (Button) findViewById(R.id.rollButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice(false);
                mediaPlayer.start();
            }
        });
        rollDice(false);

        b = (Button) findViewById(R.id.endTurnButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTurn();
            }
        });

        b = (Button) findViewById(R.id.rulesButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRules();
            }
        });

        playerTurnView = (TextView) findViewById(R.id.playerTurnView);
        scoreView = (TextView) findViewById(R.id.scoreView);
        roundScoreView = (TextView) findViewById(R.id.roundScoreView);
        topScoreView = (TextView) findViewById(R.id.topScoreView);
        updateView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public void startRules() {
        Intent intent = new Intent(getBaseContext(), HelpScreen.class);
        startActivity(intent);
    }

    private void updateView() {
        int playerTurn = scoreManager.getCurrentTurn();
        int score = scoreManager.getScore();
        int topPlayer = scoreManager.getTopPlayer();
        int topScore = scoreManager.getTopScore();
        if (scoreManager.getWinner() == -1) {
            playerTurnView.setText("Player " + String.valueOf(playerTurn + 1));
            scoreView.setText("Score: " + String.valueOf(score));
            roundScoreView.setText("Round: " + String.valueOf(scoreManager.getCurrentRoundScore()));
            topScoreView.setText("Top: Player "
                    + String.valueOf(topPlayer + 1) + "    Score: "
                    + String.valueOf(topScore));

            checkFarkle();
        } else {
            playerTurnView.setText("WIN: Player" + String.valueOf(scoreManager.getWinner()+1));
            scoreView.setText("Score: " + String.valueOf(topScore));
            roundScoreView.setText("");
            topScoreView.setText("");
        }
    }

    private void endTurn() {
        if (rollDice(true)) {
            scoreManager.endTurn(checkSelection());
            updateView();
        }
    }
}
