package com.example.haram.mimic;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
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
    private static final int FRAME_W = 200;
    // frame height
    private static final int FRAME_H = 308;
    // number of frames
    private static final int NB_FRAMES = 5;

    // nb of frames in x
    private static final int COUNT_X = 1;
    // nb of frames in y
    private static final int COUNT_Y = 5;

    //player frames holder
    static private Bitmap[] player;
    static private Bitmap[] npc;

    public Bitmap[] getPlayer() {
        return this.player;
    }

    public Bitmap[] getNpc() {
        return this.npc;
    }


    // we can slow animation by changing frame duration
    private static final int FRAME_DURATION = 400; // in ms !

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title_main = (ImageView) findViewById(R.id.title_main);

        imgOne = (ImageView) findViewById(R.id.imgOne);
        imgTwo = (ImageView) findViewById(R.id.imgTwo);


        // load bitmap from assets
        Bitmap birdBmp = getBitmapFromAssets(this, "character.png");
        Bitmap npcBmp = getBitmapFromAssets(this, "npc.png");
        player = animationSet(birdBmp, true);
        npc = animationSet(npcBmp, false);

        animationGet(imgTwo, player);
        animationGet(imgOne, npc);

        title_main.post(new Runnable() {

            @Override
            public void run() {
                ((AnimationDrawable) title_main.getBackground()).start();
            }

        });
    }

    public void startGame(View view){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
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

    private Bitmap[] animationSet(Bitmap birdBmp, boolean check){
        int y = 0;
        Bitmap[] temp = new Bitmap[NB_FRAMES];


        if (birdBmp != null) {
            // cut bitmaps from bird bmp to array of bitmaps
            temp = new Bitmap[NB_FRAMES];
            int currentFrame = 0;

            for (int i = 0; i < COUNT_Y; i++) {

                temp[currentFrame] = Bitmap.createBitmap(birdBmp, 0, FRAME_H * i, FRAME_W, FRAME_H);

                // apply scale factor
                temp[currentFrame] = Bitmap.createScaledBitmap(
                        temp[currentFrame], FRAME_W * 1, FRAME_H * 1, true);

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


}
