package pl.mateusz.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int deciseconds = 0;
    private boolean running = false;
    private final Handler handler = new Handler();
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // loading state if recreated
        if (savedInstanceState != null) {
            deciseconds = savedInstanceState.getInt("deciseconds");
            running = savedInstanceState.getBoolean("running");
        }
        // adding onClickListeners
        ImageButton buttonStart = findViewById(R.id.button_start);
        ImageButton buttonStop = findViewById(R.id.button_stop);
        ImageButton buttonReset = findViewById(R.id.button_reset);
        buttonStart.setOnClickListener(e -> onClickStart());
        buttonStop.setOnClickListener(e -> onClickStop());
        buttonReset.setOnClickListener(e -> onClickReset());
        // configuring timer runnable
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                TextView timeView = findViewById(R.id.text_time);
                if (running) {
                    deciseconds++;
                    handler.postDelayed(this, 100);
                }
                int decis = deciseconds % 10;
                int seconds = (deciseconds % 600) / 10;
                int minutes = (deciseconds % 36000) / 600;
                int hours = (deciseconds / 36000);
                String newTime = String.format(Locale.getDefault(), "%d:%02d:%02d:%d",
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

    private void onClickStart() {
        if (!running) {
            running = true;
            handler.postDelayed(timerRunnable, 100);
        }
    }

    private void onClickStop() {
        running = false;
    }

    private void onClickReset() {
        running = false;
        deciseconds = 0;
        handler.post(timerRunnable);
    }

}