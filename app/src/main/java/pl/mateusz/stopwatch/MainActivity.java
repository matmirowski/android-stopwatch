package pl.mateusz.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int deciseconds = 0;
    private int lapCounter = 0;
    private int previousLapTime = 0;
    private boolean running = false;
    private final Handler handler = new Handler();
    private Runnable timerRunnable;
    private TextView timeView;
    private ImageButton buttonStartStop;
    private ImageButton buttonReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.text_time);
        // loading state if recreated
        if (savedInstanceState != null) {
            deciseconds = savedInstanceState.getInt("deciseconds");
            running = savedInstanceState.getBoolean("running");
        }
        // adding onClickListeners
        buttonStartStop = findViewById(R.id.button_start_or_stop);
        buttonReset = findViewById(R.id.button_reset_or_lap);
        buttonStartStop.setOnClickListener(e -> onClickStartStop());
        buttonReset.setOnClickListener(e -> onClickReset());
        // configuring timer runnable
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    deciseconds++;
                    handler.postDelayed(this, 100);
                }
                int decis = deciseconds % 10;
                int seconds = (deciseconds % 600) / 10;
                int minutes = (deciseconds % 36000) / 600;
                int hours = (deciseconds / 36000);
                String newTime = String.format(Locale.getDefault(), "%d:%02d:%02d.%d",
                        hours, minutes, seconds, decis);
                timeView.setText(newTime);
            }
        };
        // executing timer runnable to display initial time
        handler.post(timerRunnable);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt("deciseconds", deciseconds);
        savedInstanceState.putBoolean("running", running);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void onClickStartStop() {
        if (!running) {
            if (deciseconds == 0) // start
                buttonReset.setVisibility(View.VISIBLE);
            running = true;
            handler.postDelayed(timerRunnable, 100);
            buttonReset.setImageResource(R.drawable.lap);
            buttonStartStop.setImageResource(R.drawable.pause);
        } else { // pause function
            running = false;
            buttonStartStop.setImageResource(R.drawable.start);
            buttonReset.setImageResource(R.drawable.reset);
        }
    }

    private void onClickReset() {
        if (!running) { // reset function
            deciseconds = 0;
            handler.post(timerRunnable);
            buttonReset.setImageResource(R.drawable.lap);
            buttonReset.setVisibility(View.GONE);
        } else { // lap function
            LinearLayout layoutLaps = findViewById(R.id.layout_laps);
            TextView lapView = new TextView(this);
            lapView.setGravity(Gravity.CENTER_HORIZONTAL);
            int difference = deciseconds - previousLapTime;
            previousLapTime = deciseconds;
        }
    }

}