package com.example.haram.mimic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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

    private ImageView boxOne;
    private ImageView boxTwo;
    private ImageView boxThree;
    private ImageView boxFour;
    private ImageView boxFive;

    private Bitmap[] npc;
    private Bitmap[] player;
    private Bitmap[] arrow;

    private boolean npcMoving = false;
    private int currentTime; // make updateAuto() return a string

    private int moveCounter = 4;
    static int moveIndex = 0;
    static ArrayList<Integer> moveList = new ArrayList<Integer>();

    private int moveChecker = 0;
    private MediaPlayer move1;
    private MediaPlayer move2;
    private MediaPlayer move3;
    private MediaPlayer move4;
    private MediaPlayer move5;
    private MediaPlayer combo;

    private MediaPlayer ending;

    private Thread timerThread;
    private Thread npcThread;

    private TextView scoreboard;
    private double currentScore = 0;
    private double comboCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        move1 = MediaPlayer.create(this, R.raw.action1);
        move2 = MediaPlayer.create(this, R.raw.action2);
        move3 = MediaPlayer.create(this, R.raw.action3);
        move4 = MediaPlayer.create(this, R.raw.action4);
        move5 = MediaPlayer.create(this, R.raw.action5);
        ending = MediaPlayer.create(this, R.raw.timeup);
        combo = MediaPlayer.create(this, R.raw.nice);

        MainActivity variables = new MainActivity();
        npc = variables.getNpc();
        player = variables.getPlayer();
        arrow = variables.getArrow();

        imgOne = (ImageView) findViewById(R.id.imgOne);
        imgTwo = (ImageView) findViewById(R.id.imgTwo);

        boxOne = (ImageView) findViewById(R.id.oneView);
        boxTwo = (ImageView) findViewById(R.id.twoView);
        boxThree = (ImageView) findViewById(R.id.threeView);
        boxFour = (ImageView) findViewById(R.id.fourView);
        boxFive = (ImageView) findViewById(R.id.fiveView);


        imgOne.setImageBitmap(npc[0]);
        imgTwo.setImageBitmap(player[0]);


        final TextView timerView = (TextView) findViewById(R.id.timer);
        scoreboard = (TextView) findViewById(R.id.scoreboard);


        currentTime = 30;
        timerView.setText("Timer: "+Integer.toString(currentTime));
        Runnable myRunnable = new Runnable() {
            public void run() {

                while(currentTime>1 )
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }

                    if(!npcMoving){
                        timerView.post(new Runnable() {
                            @Override
                            public void run() {
                                timerView.setText("Timer: "+Integer.toString(--currentTime));
                            }});
                    }
                }
                ending.start();
                Intent intent = new Intent(GameActivity.this, EndActivity.class);
                intent.putExtra("current_score", currentScore);
                startActivity(intent);

            }
        };

        final Runnable npcMovement = new Runnable() {
            public void run() {

                npcMoving = true;
                moveList.clear();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonOff();
                    }
                });


                for(int i = 0; i < moveCounter; i++) {
                    final int random = (int) (Math.random() * 4 + 1);

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    moveList.add(random);
                    final int position = i % 5;

                    imgOne.post(new Runnable() {
                        @Override
                        public void run() {
                            imgOne.setImageBitmap(npc[random]);

                            if(position == 0){
                                boxOne.setImageDrawable(null);
                                boxTwo.setImageDrawable(null);
                                boxThree.setImageDrawable(null);
                                boxFour.setImageDrawable(null);
                                boxFive.setImageDrawable(null);
                            }

                            switch(position){
                                case 0:
                                    boxOne.setImageBitmap(arrow[random-1]);
                                    break;
                                case 1:
                                    boxTwo.setImageBitmap(arrow[random-1]);
                                    break;
                                case 2:
                                    boxThree.setImageBitmap(arrow[random-1]);
                                    break;
                                case 3:
                                    boxFour.setImageBitmap(arrow[random-1]);
                                    break;
                                case 4:
                                    boxFive.setImageBitmap(arrow[random-1]);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                    switch(random){
                        case 1:
                            move1.start();
                            break;
                        case 2:
                            move2.start();
                            break;
                        case 3:
                            move3.start();
                            break;
                        case 4:
                            move4.start();
                            break;
                        default:
                            break;
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        imgOne.setImageBitmap(npc[0]);
                        buttonTurnOn();

                        boxOne.setImageDrawable(null);
                        boxTwo.setImageDrawable(null);
                        boxThree.setImageDrawable(null);
                        boxFour.setImageDrawable(null);
                        boxFive.setImageDrawable(null);

                    }
                });

                npcMoving = false;
            }
        };


        timerThread = new Thread(myRunnable);
        timerThread.start();

        npcThread = new Thread(npcMovement);
        npcThread.start();

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
                    moveChecker = checkMove(2,moveIndex);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    left.setBackgroundResource(R.drawable.btn2);
                    imgTwo.setImageBitmap(player[0]);

                    switch(moveChecker){
                        case 0:
                            move2.start();
                            moveIndex++;
                            break;

                        case 1:
                            moveIndex = 0;
                            moveCounter--;
                            comboCounter = 1;
                            if(moveCounter < 4)
                                moveCounter = 4;

                            move5.start();
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            imgTwo.setImageBitmap(player[5]);


                            break;

                        case 2:
                            move2.start();
                            moveIndex = 0;
                            moveCounter++;
                            comboCounter += 0.5;
                            scoreCounter();
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            break;

                        default:
                            break;
                    }
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
                    moveChecker = checkMove(1,moveIndex);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    right.setBackgroundResource(R.drawable.btn1);
                    imgTwo.setImageBitmap(player[0]);

                    switch(moveChecker){
                        case 0:
                            move1.start();
                            moveIndex++;
                            break;

                        case 1:
                            moveIndex = 0;
                            moveCounter--;
                            comboCounter = 1;
                            if(moveCounter < 4)
                                moveCounter = 4;

                            move5.start();
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            imgTwo.setImageBitmap(player[5]);

                            break;

                        case 2:
                            move1.start();
                            moveIndex = 0;
                            moveCounter++;
                            comboCounter += 0.5;
                            scoreCounter();
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            break;

                        default:
                            break;
                    }
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
                    moveChecker = checkMove(4,moveIndex);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    top.setBackgroundResource(R.drawable.btn4);
                    imgTwo.setImageBitmap(player[0]);

                    switch(moveChecker){
                        case 0:
                            move4.start();
                            moveIndex++;
                            break;

                        case 1:
                            moveIndex = 0;
                            moveCounter--;
                            comboCounter = 1;
                            if(moveCounter < 4)
                                moveCounter = 4;

                            move5.start();
                            imgTwo.setImageBitmap(player[5]);
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            break;

                        case 2:
                            move4.start();
                            moveIndex = 0;
                            moveCounter++;
                            comboCounter += 0.5;
                            scoreCounter();
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            break;

                        default:
                            break;
                    }
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
                    moveChecker = checkMove(3,moveIndex);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    bot.setBackgroundResource(R.drawable.btn3);
                    imgTwo.setImageBitmap(player[0]);

                    switch(moveChecker){
                        case 0:
                            move3.start();
                            moveIndex++;
                            break;

                        case 1:
                            moveIndex = 0;
                            moveCounter--;
                            comboCounter = 1;
                            if(moveCounter < 4)
                                moveCounter = 4;

                            move5.start();
                            imgTwo.setImageBitmap(player[5]);
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            break;

                        case 2:
                            move3.start();
                            moveIndex = 0;
                            moveCounter++;
                            comboCounter += 0.5;
                            scoreCounter();
                            npcThread.interrupt();  // if you need to make sure thread's run() method stops ASAP
                            npcThread = new Thread(npcMovement);
                            npcThread.start();
                            break;

                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    public int checkMove(int moveNumber, int index){
        if(!moveList.get(index).equals(moveNumber)){
            return 1;
        }

        if(index == moveList.size()-1){
            return 2;
        }

        return 0;
    }

    public void buttonOff(){

        left.setEnabled(false);
        top.setEnabled(false);
        right.setEnabled(false);
        bot.setEnabled(false);
    }

    public void buttonTurnOn(){

        left.setEnabled(true);
        top.setEnabled(true);
        right.setEnabled(true);
        bot.setEnabled(true);
    }

    public void scoreCounter(){

        currentScore += 1000 * comboCounter;

        if(comboCounter > 1)
            combo.start();

        scoreboard.setText("Score: "+Double.toString(currentScore));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);

    }

}
