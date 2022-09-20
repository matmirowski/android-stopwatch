package pl.mateusz.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int deciseconds = 0;
    private int lapCounter = 0;
    private int previousLapTime = 0;
    private boolean running = false;
    private final Handler handler = new Handler();
    private ArrayList<String> htmlLapList = new ArrayList<>();
    private Runnable timerRunnable;
    private TextView timeView;
    private ImageButton buttonStartStop;
    private ImageButton buttonResetLap;
    private ScrollView scrollView;
    private LinearLayout layoutLaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        assignViewsToVariables();
        // loading state if recreated
        if (savedInstanceState != null)
                this.loadSavedInstanceState(savedInstanceState);
        // adding onClickListeners
        buttonStartStop.setOnClickListener(e -> onClickStartStop());
        buttonResetLap.setOnClickListener(e -> onClickResetLap());
        // configuring timer runnable
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    deciseconds++;
                    handler.postDelayed(this, 100);
                }
                timeView.setText(formatToTime(deciseconds));
            }
        };
        // executing timer runnable to display initial time
        handler.post(timerRunnable);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt("deciseconds", deciseconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putInt("lapCounter", lapCounter);
        savedInstanceState.putInt("previousLapTime", previousLapTime);
        savedInstanceState.putStringArrayList("lapList", htmlLapList);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void onClickStartStop() {
        if (!running) {
            if (deciseconds == 0) { // start
                Animation animation6 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_center_out);
                buttonStartStop.startAnimation(animation6);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonResetLap.setImageResource(R.drawable.lap);
                        buttonStartStop.setImageResource(R.drawable.pause);
                        buttonResetLap.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_left_in);
                        buttonResetLap.startAnimation(animation);
                        Animation animation2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_right_in);
                        buttonStartStop.startAnimation(animation2);
                    }
                }, 300);
            }
            running = true;
            handler.postDelayed(timerRunnable, 100);
        } else { // pause
            running = false;
            buttonStartStop.setImageResource(R.drawable.start);
            buttonResetLap.setImageResource(R.drawable.reset);
        }
    }

    private void onClickResetLap() {
        if (!running) { // reset function
            this.reset();
        } else { // lap function
            this.addLap();
        }
    }

    private void reset() {
        deciseconds = 0;
        handler.post(timerRunnable);
        buttonResetLap.setVisibility(View.GONE);
        layoutLaps.removeAllViews();
        layoutLaps.setVisibility(View.INVISIBLE);
        lapCounter = 0;
        previousLapTime = 0;
        htmlLapList.clear();
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        buttonStartStop.startAnimation(animation3);
        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        buttonResetLap.startAnimation(animation4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation5 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_center_in);
                buttonStartStop.startAnimation(animation5);
            }
        }, 300);
    }

    private void addLap() {
        int difference = deciseconds - previousLapTime;
        previousLapTime = deciseconds;
        lapCounter++;
        String formatedDifference = "&#160&#160&#160&#160&#160&#160&#160&#160<font color=#FF0000>" + "+"
                + formatToTime(difference)
                + "&#160&#160&#160&#160&#160&#160&#160&#160</font>";
        String formatedCurrentTime = "<font color=#FFFFFF>" + formatToTime(deciseconds)
                + "</font>";
        String formatedLapCounter = String.format(Locale.getDefault(), "%02d",
                lapCounter);
        String htmlLap = formatedLapCounter + formatedDifference + formatedCurrentTime;
        htmlLapList.add(htmlLap);
        this.createLapTextView(htmlLap);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private String formatToTime(int decisecs) {
        int decis = (decisecs % 10);
        int seconds = (decisecs % 600) / 10;
        int minutes = (decisecs % 36000) / 600;
        int hours = (decisecs / 36000);
        return String.format(Locale.getDefault(), "%d:%02d:%02d.%d",
                hours, minutes, seconds, decis);
    }

    private void createLapTextView(String htmlText) {
        TextView lapView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(10,10,10,10);
        lapView.setLayoutParams(params);
        lapView.setText(Html.fromHtml(htmlText));
        lapView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        layoutLaps.setVisibility(View.VISIBLE);
        layoutLaps.addView(lapView);
    }

    private void loadSavedInstanceState(Bundle savedInstanceState) {
        deciseconds = savedInstanceState.getInt("deciseconds");
        running = savedInstanceState.getBoolean("running");
        lapCounter = savedInstanceState.getInt("lapCounter");
        previousLapTime = savedInstanceState.getInt("previousLapTime");
        htmlLapList = savedInstanceState.getStringArrayList("lapList");
        for (String lap : htmlLapList) {
            this.createLapTextView(lap);
        }
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        if (running) {
            buttonStartStop.setImageResource(R.drawable.pause);
            buttonResetLap.setVisibility(View.VISIBLE);
            buttonResetLap.setImageResource(R.drawable.lap);
        } else if (deciseconds != 0) {
            buttonResetLap.setVisibility(View.VISIBLE);
            buttonResetLap.setImageResource(R.drawable.reset);
        }
    }

    private void assignViewsToVariables() {
        timeView = findViewById(R.id.text_time);
        layoutLaps = findViewById(R.id.layout_laps);
        scrollView = findViewById(R.id.scrollview_laps);
        buttonStartStop = findViewById(R.id.button_start_or_stop);
        buttonResetLap = findViewById(R.id.button_reset_or_lap);
    }

}