package com.example.haram.mimic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Haram on 2017-01-11.
 */

public class GameActivity extends AppCompatActivity {

    private ImageView imgOne;
    private ImageView imgTwo;

    private ImageButton left;
    private ImageButton top;
    private ImageButton bot;
    private ImageButton right;

    private Bitmap[] npc;
    private Bitmap[] player;
    private boolean shouldContinue = true;
    private int currentTime; // make updateAuto() return a string
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        MainActivity variables = new MainActivity();
        npc = variables.getNpc();
        player = variables.getPlayer();

        imgOne = (ImageView) findViewById(R.id.imgOne);
        imgTwo = (ImageView) findViewById(R.id.imgTwo);

        imgOne.setImageBitmap(npc[0]);
        imgTwo.setImageBitmap(player[0]);






        final TextView timerView = (TextView) findViewById(R.id.timer); //grab your tv
        currentTime = 60;
        timerView.setText("Timer: "+Integer.toString(currentTime));
        Runnable myRunnable = new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                while(currentTime>1 || shouldContinue == true)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }

                    timerView.post(new Runnable() {
                        @Override
                        public void run() {
                            timerView.setText("Timer: "+Integer.toString(--currentTime));
                        }});

                }

            }
        };

        final Thread timerThread = new Thread(myRunnable);
        //timerThread.start();
        //timerThread.interrupt();

        left = (ImageButton) findViewById(R.id.leftButton);
        right = (ImageButton) findViewById(R.id.rightButton);
        top = (ImageButton) findViewById(R.id.topButton);
        bot = (ImageButton) findViewById(R.id.bottomButton);


        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    left.setBackgroundResource(R.drawable.pbtn2);
                    imgTwo.setImageBitmap(player[2]);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    left.setBackgroundResource(R.drawable.btn2);
                    imgTwo.setImageBitmap(player[0]);
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    right.setBackgroundResource(R.drawable.pbtn1);
                    imgTwo.setImageBitmap(player[1]);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    right.setBackgroundResource(R.drawable.btn1);
                    imgTwo.setImageBitmap(player[0]);

                }
                return false;
            }
        });

        top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    top.setBackgroundResource(R.drawable.pbtn4);
                    imgTwo.setImageBitmap(player[4]);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    top.setBackgroundResource(R.drawable.btn4);
                    imgTwo.setImageBitmap(player[0]);

                }
                return false;
            }
        });




        bot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bot.setBackgroundResource(R.drawable.pbtn3);
                    imgTwo.setImageBitmap(player[3]);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    bot.setBackgroundResource(R.drawable.btn3);
                    imgTwo.setImageBitmap(player[0]);

                }
                return false;
            }
        });



    }

    public void startgame(){



        Runnable moving = new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                int moveCounter = 4;
                ArrayList<Integer> moveList = new ArrayList<Integer>();
                for(int i = 0; i < moveCounter; i++){
                    final int random = (int )(Math.random() * 4 + 1);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    moveList.add(random);

                    imgOne.post(new Runnable() {
                        @Override
                        public void run() {
                            imgOne.setImageBitmap(npc[random]);
                        }});

                }

            }
        };



        final Thread moveThread = new Thread(moving);
        moveThread.start();
    }
}
