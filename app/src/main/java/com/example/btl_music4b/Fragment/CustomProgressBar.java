package com.example.btl_music4b.Fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.btl_music4b.R;

public class CustomProgressBar extends LinearLayout {
    View loaderCircle;
    boolean stopAnimation = false;
    Animation rotation,animationOut;

    public CustomProgressBar(Context context) {
        super(context);
        rotation = AnimationUtils.loadAnimation(context, R.anim.n_rotation_repeat);
        animationOut = AnimationUtils.loadAnimation(context,R.anim.n_animation_progress_out);
        animationOut.setAnimationListener(listener);
        inflate(context, R.layout.custom_progressbar,this);
        loaderCircle = findViewById(R.id.loader_circle);
        runAnimation();
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rotation = AnimationUtils.loadAnimation(context, R.anim.n_rotation_repeat);
        animationOut = AnimationUtils.loadAnimation(context,R.anim.n_animation_progress_out);
        animationOut.setAnimationListener(listener);
        inflate(context, R.layout.custom_progressbar,this);
        loaderCircle = findViewById(R.id.loader_circle);
        runAnimation();
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rotation = AnimationUtils.loadAnimation(context, R.anim.n_rotation_repeat);
        animationOut = AnimationUtils.loadAnimation(context,R.anim.n_animation_progress_out);
        animationOut.setAnimationListener(listener);
        inflate(context, R.layout.custom_progressbar,this);
        loaderCircle = findViewById(R.id.loader_circle);
        runAnimation();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        rotation = AnimationUtils.loadAnimation(context, R.anim.n_rotation_repeat);
        animationOut = AnimationUtils.loadAnimation(context,R.anim.n_animation_progress_out);
        animationOut.setAnimationListener(listener);
        inflate(context, R.layout.custom_progressbar,this);
        loaderCircle = findViewById(R.id.loader_circle);
        runAnimation();
    }


    private void runAnimation(){
        loaderCircle.startAnimation(rotation);
    }
    public void startAnimation(){
        stopAnimation = false;
        runAnimation();
    }
    public void startAnimationOut(){
        startAnimation(animationOut);
    }

    Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rotation.cancel();
            setVisibility(GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    @Override
    public void setVisibility(int visibility) {
        if(visibility==VISIBLE) {
            super.setVisibility(visibility);
            stopAnimation = false;
            runAnimation();
        }else {
            if(stopAnimation) {
                super.setVisibility(visibility);
            }else {
                stopAnimation = true;
                startAnimationOut();
            }
        }
    }
}
