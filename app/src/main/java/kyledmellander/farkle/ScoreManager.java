package kyledmellander.farkle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Kyle on 5/10/2016.
 */
public class ScoreManager {
    private static ScoreManager singleton;
    private static int numPlayers;
    private static int[] scores;
    private int currentPlayer;
    private int currentRoundScore;
    private static final String TAG = Game.class.getSimpleName();

    private ScoreManager(int n) {
        numPlayers = n;
        scores = new int[n];
        currentPlayer = 0;
    }

    public void newGame(int n) {
        numPlayers = n;
        scores = new int[n];
        currentPlayer = 0;
    }

    public boolean checkFarkle(ArrayList<Die> remainingDice) {
        int[] diceCounts = new int[6];
        for (Die die : remainingDice) {
            diceCounts[die.getValue()-1]++;
        }
        boolean farkled = true;
        boolean twoTrips = hasTwoTrips(diceCounts);
        boolean threePair = hasThreePair(diceCounts);
        boolean fourAny = hasSetSize(diceCounts,4);
        boolean fourAndPair = fourAny && hasSetSize(diceCounts,2);
        boolean straight = hasStraight(diceCounts);
        boolean fiveAny = hasSetSize(diceCounts,5);
        boolean sixAny = hasSetSize(diceCounts,6);

        //Handle unique cases
        if (sixAny) farkled = false;
        if (twoTrips) farkled = false;
        if (fourAndPair) farkled = false;
        if (threePair) farkled = false;
        if (straight) farkled = false;
        if (fiveAny) farkled = false;
        if (fourAny) farkled = false;
        if (hasSetSize(diceCounts,3)) farkled = false;
        if (diceCounts[0] != 0 || diceCounts[4] != 0) farkled = false;

        if (farkled) {
            currentRoundScore = 0;
        }
        return farkled;
    }

    public int calculateScore(ArrayList<Die> selectedDice) {
        int score = 0;
        int[] diceCounts = new int[6];
        boolean threePair, fourAny, fiveAny, sixAny, fourAndPair, twoTrips, straight;
        boolean regular;
        for (Die die : selectedDice) {
            diceCounts[die.getValue()-1]++;
        }

        regular = isRegular(diceCounts);
        twoTrips = hasTwoTrips(diceCounts);
        threePair = hasThreePair(diceCounts);
        fourAny = hasSetSize(diceCounts,4);
        fourAndPair = fourAny && hasSetSize(diceCounts,2);
        straight = hasStraight(diceCounts);
        fiveAny = hasSetSize(diceCounts,5);
        sixAny = hasSetSize(diceCounts,6);

        //Handle unique cases
        if (sixAny) return 3000;
        if (twoTrips) return 2500;
        if (fourAndPair) return 1500;
        if (threePair) return 1500;
        if (straight) return 1500;

        //Handle sets
        if (regular) {
            //Check for 5 of a kind, and add extra 1s/5s if necessary
            if (fiveAny) {
                if (diceCounts[0] == 1) return 2000 + 100;
                if (diceCounts[4] == 1) return 2000 + 50;
            }
            //Check for 4 of a kind and add extra 1s/5s if necessary
            if (fourAny) {
                if (diceCounts[0] <= 2) {
                    score += diceCounts[0] * 100;
                }
                if (diceCounts[4] <= 2) {
                    score += diceCounts[1] * 50;
                }
                return 1000 + score;
            }

            //Add 100 for every 1
            score += 100 * diceCounts[0];
            //Add 50 for every 5 if there isn't a set of 5s
            if (diceCounts[4] < 3) score += 50*diceCounts[4];

            for (int diceIndex = 1; diceIndex < 6; diceIndex++) {
                if (diceCounts[diceIndex] == 3) score+= 100*(diceIndex+1);
            }
        }
        return score;
    }

    //Ignoring 1s and fives, return true if there are no sets less than size 3
    private boolean isRegular(int[] diceCounts) {
        for (int dieValue = 1; dieValue < 6; dieValue++) {
            if (diceCounts[dieValue] < 3 && diceCounts[dieValue] != 0 && dieValue != 4) {
                return false;
            }
        }
        return true;
    }

    private boolean hasSetSize(int[] diceCounts, int setSize) {
        for (int numDice : diceCounts) {
            if (numDice == setSize) return true;
        }
        return false;
    }

    private boolean hasTwoTrips(int[] diceCounts) {
        int numTrips = 0;
        for (int numDice : diceCounts) {
           if (numDice == 3) numTrips++;
        }

        return numTrips == 2;
    }

    private boolean hasThreePair(int[] diceCounts) {
        int numPairs = 0;
        for (int numDice : diceCounts) {
            if (numDice == 2) numPairs++;
        }
        return numPairs == 3;
    }

    private boolean hasStraight(int[] diceCounts) {
        for (int numDice : diceCounts) {
            if (numDice != 1) return false;
        }
        return true;
    }

    public int getCurrentTurn() {
        return currentPlayer;
    }

    public int getCurrentRoundScore() {return currentRoundScore;}

    public int endTurn(int finalSelectionScore) {
        scores[currentPlayer] += (currentRoundScore + finalSelectionScore);
        currentPlayer = (currentPlayer + 1) % numPlayers;
        currentRoundScore = 0;
        return currentPlayer;
    }

    public static ScoreManager getScoreManager(int n) {
        if (singleton == null || singleton.numPlayers != n) {
           singleton = new ScoreManager(n);
        }
        return singleton;
    }

    public static void reset() {
        singleton = null;
    }

    public int getScore() {
       return scores[currentPlayer];
    }

    public int getTopScore() {
        int topScore = -1;
        for (int score : scores) {
            if (score > topScore) {
                topScore = score;
            }
        }
        return topScore;
    }


    public int getTopPlayer() {
        int topScore = -1;
        int score;
        int topPlayer = 0;
        for (int player = 0; player < numPlayers; player++) {
            score = scores[player];
            if (score > topScore) {
                topScore = score;
                topPlayer = player;
            }
        }
        return topPlayer;
    }

    public int addSelected(ArrayList<Die> selectedDice) {
        currentRoundScore += calculateScore(selectedDice);
        return currentRoundScore;
    }

    //Returns -1 if there is no Winner, or the playerID (0-3) if there is a winner
    public int getWinner() {
        int winner = -1;
        for (int player = 0; player < numPlayers; player++) {
            if (scores[player] > 10000) {
                return player;
            }
        }
        return winner;
    }
}
