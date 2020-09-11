package com.java.caisijie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.java.caisijie.R;

import java.util.Random;

public class WelcomeActivity extends Activity {

    ImageView mBackgroundImage;
    TextView mTitleText;
    TextView mVersionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        //隐藏导航栏
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(newUiOptions);

        mBackgroundImage = (ImageView) findViewById(R.id.image_background);
        Random random = new Random();
        int num = random.nextInt(2);
        int drawable = R.drawable.pic_background_1;
        switch (num){
            case 0:
                break;
            case 1:
                drawable = R.drawable.pic_background_4;
                break;

        }
        mBackgroundImage.setImageDrawable(getResources().getDrawable(drawable));
        // TODO
        // android:duration="0" => android:duration="3000"
        Animation animImage = AnimationUtils.loadAnimation(this,R.anim.image_welcom);
        mBackgroundImage.startAnimation(animImage);
        animImage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束时打开首页
//                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                //overridePendingTransition(R.anim.activity_slide_in, R.anim.no_anim);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mTitleText = (TextView) findViewById(R.id.title_text);
    }

    @Override
    public void finish() {
        mBackgroundImage.destroyDrawingCache();
        super.finish();
    }
}