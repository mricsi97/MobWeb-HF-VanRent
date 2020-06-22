package hu.bme.aut.vanrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimeSetActivity extends AppCompatActivity {

    public static final String TIME_SLOT_START_HOUR = "TIME_SLOT_START_HOUR";
    public static final String TIME_SLOT_END_HOUR = "TIME_SLOT_END_HOUR";
    public static final String PICKED_INTERVAL = "PICKED_INTERVAL";


    private int minHour;
    private int maxHour;

    private Button btnSetTime;
    private TimePicker tpStartTime;
    private TimePicker tpEndTime;
    private TextView tvInterval;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ListActivity.theme);
        setContentView(R.layout.activity_timeset);

        minHour = getIntent().getIntExtra(TIME_SLOT_START_HOUR, 0);
        maxHour = getIntent().getIntExtra(TIME_SLOT_END_HOUR, 0);

        btnSetTime = findViewById(R.id.btnSetTime);
        tpStartTime = findViewById(R.id.tpStartTime);
        tpEndTime = findViewById(R.id.tpEndTime);
        tvInterval = findViewById(R.id.tvInterval);

        tpStartTime.setIs24HourView(true);
        tpStartTime.setCurrentHour(minHour);
        tpStartTime.setCurrentMinute(0);
        tpEndTime.setIs24HourView(true);
        tpEndTime.setCurrentHour(maxHour);
        tpEndTime.setCurrentMinute(0);

        tvInterval.setText(getString(R.string.time_interval, minHour, maxHour));

        tpStartTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tvInterval.setText(getString(R.string.time_interval, hourOfDay, tpEndTime.getCurrentHour()));
            }
        });

        tpEndTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tvInterval.setText(getString(R.string.time_interval, tpStartTime.getCurrentHour(), hourOfDay));
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentStart = tpStartTime.getCurrentHour();
                int currentEnd = tpEndTime.getCurrentHour();
                if(currentStart < minHour) {
                    Toast.makeText(TimeSetActivity.this, getString(R.string.wrong_start_time, minHour), Toast.LENGTH_LONG).show();
                    return;
                }
                if(currentEnd > maxHour) {
                    Toast.makeText(TimeSetActivity.this, getString(R.string.wrong_end_time, maxHour), Toast.LENGTH_LONG).show();
                    return;
                }
                if(currentEnd <= currentStart){
                    Toast.makeText(TimeSetActivity.this, R.string.wrong_times, Toast.LENGTH_LONG).show();
                    return;
                }

                sendInterval(currentStart, currentEnd);

                Intent intent = new Intent();
                intent.setClass(TimeSetActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendInterval(int start, int end){
        Intent intent = new Intent("reservation-event");
        int[] interval = {start, end};
        intent.putExtra(PICKED_INTERVAL, interval);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
