package kyledmellander.farkle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class TitleScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int numPlayers = 1;

    private static class MySpinnerAdapter<String> extends ArrayAdapter<String> {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/minecraft.ttf");

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position,convertView, parent);
            view.setTypeface(font);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,convertView,parent);
            view.setTypeface(font);
            return view;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/minecraft.ttf");
        //TextView titleTextView = (TextView) findViewById(R.id.titleText);
        //titleTextView.setTypeface(font);

        initSpinner();
        initStartGameButton();
        initResetGameButton();
        initRulesButton();
    }


    public void onItemSelected(AdapterView<?> parent, View view,
        int pos, long id) {
        //An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        numPlayers = pos+1;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Another interface callback
    }

    public void startGame() {
        Intent intent = new Intent(getBaseContext(), Game.class);
        intent.putExtra("numPlayers",numPlayers);
        startActivity(intent);
    }

    public void startRules() {
        Intent intent = new Intent(getBaseContext(), HelpScreen.class);
        startActivity(intent);
    }

    public void resetGame() {
        ScoreManager.reset();
    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        MySpinnerAdapter<String> adapter1 = new MySpinnerAdapter(
                getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item,
                Arrays.asList(getResources().getStringArray(R.array.Players))
        );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Players, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
    }

    private void initStartGameButton() {
        Button startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startGame();
            }
        });
    }

    private void initResetGameButton() {
        Button resetGameButton = (Button) findViewById(R.id.resetButton);
        resetGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    private void initRulesButton() {
        Button startGameButton = (Button) findViewById(R.id.rulesButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startRules();
            }
        });
    }
}
