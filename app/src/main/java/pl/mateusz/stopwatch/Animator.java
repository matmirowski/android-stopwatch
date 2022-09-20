package pl.mateusz.stopwatch;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class Animator {
    private final ImageButton buttonStartStop;
    private final ImageButton buttonResetLap;
    private final Context appContext;
    private Animation animSlideRightOut;
    private Animation animSlideRightIn;
    private Animation animSlideLeftOut;
    private Animation animSlideLeftIn;
    private Animation animSlideDown;
    private Animation animSlideUp;

    public Animator(ImageButton buttonStartStop, ImageButton buttonResetLap, Context context) {
        this.buttonStartStop = buttonStartStop;
        this.buttonResetLap = buttonResetLap;
        this.appContext = context;
        loadAnimations();
    }

    public void playResetAnimation() {
        buttonStartStop.startAnimation(animSlideRightOut);
        buttonResetLap.startAnimation(animSlideLeftOut);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonStartStop.startAnimation(animSlideUp);
            }
        }, 300);
    }

    public void playStartAnimation() {
        buttonStartStop.startAnimation(animSlideDown);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonResetLap.setVisibility(View.VISIBLE);
                buttonResetLap.startAnimation(animSlideLeftIn);
                buttonStartStop.setImageResource(R.drawable.pause);
                buttonStartStop.startAnimation(animSlideRightIn);
            }
        }, 300);
    }

    private void loadAnimations() {
        animSlideRightOut = AnimationUtils.loadAnimation(appContext, R.anim.slide_right_out);
        animSlideRightIn = AnimationUtils.loadAnimation(appContext, R.anim.slide_right_in);
        animSlideLeftOut = AnimationUtils.loadAnimation(appContext, R.anim.slide_left_out);
        animSlideLeftIn = AnimationUtils.loadAnimation(appContext, R.anim.slide_left_in);
        animSlideDown = AnimationUtils.loadAnimation(appContext, R.anim.slide_center_out);
        animSlideUp = AnimationUtils.loadAnimation(appContext, R.anim.slide_center_in);
    }
}
