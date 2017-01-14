package com.example.haram.mimic;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView title_main;

    private ImageView imgOne;
    private ImageView imgTwo;


    // frame width
    private static int FRAME_W = 200;
    // frame height
    private static int FRAME_H = 308;
    // number of frames
    private static int NB_FRAMES = 5;

    // nb of frames in y
    private static int COUNT_Y = 5;

    //player frames holder
    static private Bitmap[] player;
    static private Bitmap[] npc;
    static private Bitmap[] arrow;

    public Bitmap[] getPlayer() {
        return this.player;
    }

    public Bitmap[] getNpc() {
        return this.npc;
    }

    public Bitmap[] getArrow() {
        return this.arrow;
    }


    // we can slow animation by changing frame duration
    private static final int FRAME_DURATION = 400; // in ms !

    private MediaPlayer intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intro = MediaPlayer.create(this, R.raw.main_bgm);
        intro.setLooping(true);
        intro.start();

        title_main = (ImageView) findViewById(R.id.title_main);

        imgOne = (ImageView) findViewById(R.id.imgOne);
        imgTwo = (ImageView) findViewById(R.id.imgTwo);


        // load bitmap from assets
        Bitmap playerBmp = getBitmapFromAssets(this, "character.png");
        Bitmap npcBmp = getBitmapFromAssets(this, "npc.png");
        Bitmap arrowBmp = getBitmapFromAssets(this, "arrow.png");

        npc = animationSet(npcBmp);

        COUNT_Y = 6;
        NB_FRAMES = 6;
        player = animationSet(playerBmp);

        COUNT_Y = 4;
        FRAME_W = 80;
        FRAME_H = 87;
        NB_FRAMES = 4;
        arrow = animationSet(arrowBmp);

        NB_FRAMES = 5;
        animationGet(imgTwo, player);
        animationGet(imgOne, npc);

        title_main.post(new Runnable() {

            @Override
            public void run() {
                ((AnimationDrawable) title_main.getBackground()).start();
            }

        });


        FRAME_W = 200;
        FRAME_H = 308;
        NB_FRAMES = 5;
        COUNT_Y = 5;


    }

    public void startGame(View view){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intro.stop();
        startActivity(intent);
    }

    public void tutorialGame(View view){
        Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
        intro.stop();
        startActivity(intent);
    }

    private Bitmap getBitmapFromAssets(MainActivity mainActivity,
                                       String filepath) {
        AssetManager assetManager = mainActivity.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;

        try {
            istr = assetManager.open(filepath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException ioe) {
            // manage exception
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException e) {
                }
            }
        }

        return bitmap;
    }

    private Bitmap[] animationSet(Bitmap birdBmp){
        int y = 0;
        Bitmap[] temp = new Bitmap[NB_FRAMES];


        if (birdBmp != null) {
            // cut bitmaps from bird bmp to array of bitmaps
            temp = new Bitmap[NB_FRAMES];
            int currentFrame = 0;

            for (int i = 0; i < COUNT_Y; i++) {

                temp[currentFrame] = Bitmap.createBitmap(birdBmp, 0, FRAME_H * i, FRAME_W, FRAME_H);

//                // apply scale factor
//                temp[currentFrame] = Bitmap.createScaledBitmap(
//                        temp[currentFrame], FRAME_W * 1, FRAME_H * 1, true);

                if (++currentFrame >= NB_FRAMES) {
                    break;
                }

            }
        }

        return temp;
    }


    public void animationGet(ImageView img, Bitmap[] character){

        ImageView characterImg = img;


        // create animation programmatically
        final AnimationDrawable animation2 = new AnimationDrawable();
        animation2.setOneShot(false); // repeat animation

        for (int i = 0; i < NB_FRAMES; i++) {
            animation2.addFrame(new BitmapDrawable(getResources(), character[i]),
                    FRAME_DURATION);
        }

        // load animation on image
        if (Build.VERSION.SDK_INT < 16) {
            characterImg.setBackgroundDrawable(animation2);
        } else {
            characterImg.setBackground(animation2);
        }

        animation2.run();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(intro.isPlaying())
            intro.pause();
        else
            return;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(intro.isPlaying())
            intro.stop();
        else
            return;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!intro.isPlaying())
            intro.start();
        else
            return;
    }

    @Override
    public void onBackPressed() {
    }
}
