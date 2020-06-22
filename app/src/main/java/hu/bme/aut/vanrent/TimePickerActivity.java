package hu.bme.aut.vanrent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

import hu.bme.aut.vanrent.adapter.ReservationAdapter;
import hu.bme.aut.vanrent.data.Reservation;
import hu.bme.aut.vanrent.data.ReservationDatabase;
import hu.bme.aut.vanrent.data.TimeSlot;
import hu.bme.aut.vanrent.data.VanItem;
import hu.bme.aut.vanrent.fragments.DatePickerDialogFragment;

public class TimePickerActivity extends AppCompatActivity
        implements DatePickerDialogFragment.OnDateSelectedListener,
        ReservationAdapter.TimeSlotClickedListener {

    public static final String VAN_ID = "";

    private VanItem vanItem;
    private ReservationDatabase reservationDatabase;
    private ReservationAdapter reservationAdapter;
    private RecyclerView recyclerView;

    int yearSelected;
    int monthSelected;
    int daySelected;

    private ViewSwitcher viewSwitcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ListActivity.theme);
        setContentView(R.layout.activity_timepicker);

        viewSwitcher = findViewById(R.id.viewSwitcher);

        LocalBroadcastManager.getInstance(this).registerReceiver(reservationEventReceiver,
                new IntentFilter("reservation-event"));

        vanItem = getIntent().getParcelableExtra(VAN_ID);
        reservationDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ReservationDatabase.class, "reservation-list"
        ).build();
        reservationAdapter = new ReservationAdapter(this, this);

        new DatePickerDialogFragment().show(getSupportFragmentManager(), "DATE_TAG");
        loadReservationsInBackground();
    }

    private BroadcastReceiver reservationEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int[] interval = intent.getIntArrayExtra(TimeSetActivity.PICKED_INTERVAL);
            if(interval != null)
                reserveTime(interval[0], interval[1]);
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reservationEventReceiver);
        super.onDestroy();
    }

    private void initRecycleView(){
        recyclerView = findViewById(R.id.rvTimeSlot);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reservationAdapter);
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        yearSelected = year;
        monthSelected = month;
        daySelected = day;
        int numberOfTimeSlots = reservationAdapter.calculateTimeSlots(yearSelected, monthSelected, daySelected, vanItem);
        initRecycleView();
        switchViews(numberOfTimeSlots);
    }

    void switchViews(int n){
        if (n == 0) {
            if (R.id.tvNoTimeSlots == viewSwitcher.getNextView().getId()) {
                viewSwitcher.showNext();
            }
        } else if (R.id.rvTimeSlot == viewSwitcher.getNextView().getId()) {
            viewSwitcher.showNext();
        }
    }

    private void loadReservationsInBackground(){
        new AsyncTask<Void, Void, List<Reservation>>() {

            @Override
            protected List<Reservation> doInBackground(Void... voids) {
                return reservationDatabase.reservationDao().getAllSortByStartHour();
            }

            @Override
            protected void onPostExecute(List<Reservation> reservations) {
                reservationAdapter.update(reservations);
            }

        }.execute();
    }

    @Override
    public void onTimeSlotClicked(TimeSlot timeSlot) {
        Intent intent = new Intent();
        intent.setClass(TimePickerActivity.this, TimeSetActivity.class);
        intent.putExtra(TimeSetActivity.TIME_SLOT_START_HOUR, timeSlot.startHour);
        intent.putExtra(TimeSetActivity.TIME_SLOT_END_HOUR, timeSlot.endHour);
        startActivity(intent);
    }

    private void reserveTime(int startTime, int endTime){
        final Reservation newRes = new Reservation(yearSelected, monthSelected, daySelected,
                startTime, endTime, vanItem.id);
        new AsyncTask<Void, Void, Reservation>() {

            @Override
            protected Reservation doInBackground(Void... voids) {
                newRes.id = reservationDatabase.reservationDao().insert(newRes);
                return newRes;
            }

            @Override
            protected void onPostExecute(Reservation reservation) {
                reservationAdapter.addReservation(reservation);
            }
        }.execute();
    }
}
