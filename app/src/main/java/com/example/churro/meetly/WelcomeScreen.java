package com.example.churro.meetly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.animation.ValueAnimator;


public class WelcomeScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        final TextView name = (TextView) findViewById(R.id.txt_appName);

        //App name with two different colors
        Spannable colorChange =  new SpannableString(name.getText());
        colorChange.setSpan(new ForegroundColorSpan(Color.RED), 2, "Mee".length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        name.setText(colorChange);



        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        name.startAnimation(fade);

        //rotate only along x-axis
        final ValueAnimator rotate = ValueAnimator.ofFloat(0, 1);
        rotate.setStartDelay(3500);
        rotate.setDuration(1500);
        rotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator pAnimation) {
                float value = (Float) (pAnimation.getAnimatedValue());
                name.setRotationX(360 * value);
            }
        });
        rotate.start();


        rotate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                startActivity(new Intent(WelcomeScreen.this,
                        EventList.class));
                WelcomeScreen.this.finish();
            }
        });


        final Button skip = (Button) findViewById(R.id.btn_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, EventList.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }


}