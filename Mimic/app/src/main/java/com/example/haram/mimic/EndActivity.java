package com.example.haram.mimic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.R.attr.defaultValue;

/**
 * Created by Haram on 2017-01-12.
 */

public class EndActivity extends AppCompatActivity {

    private TextView finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endpage_main);

        Intent receiveIntent = this.getIntent();
        double score = receiveIntent.getDoubleExtra("current_score", defaultValue);

        finalScore = (TextView) findViewById(R.id.scoreText);

        finalScore.setText("Score: "+Double.toString(score));




    }

    public void restartGame(View view){
        Intent intent = new Intent(EndActivity.this, GameActivity.class);
        startActivity(intent);

    }

    public void mainMenu(View view){
        Intent intent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
