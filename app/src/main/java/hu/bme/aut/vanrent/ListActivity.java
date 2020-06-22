package hu.bme.aut.vanrent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hu.bme.aut.vanrent.adapter.VanAdapter;
import hu.bme.aut.vanrent.data.ReservationDatabase;
import hu.bme.aut.vanrent.data.VanItem;

public class ListActivity extends AppCompatActivity
        implements VanAdapter.VanItemClickListener {

    public static int theme;
    private RecyclerView recyclerView;
    private VanAdapter adapter;

    private ReservationDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DateFormat df = new SimpleDateFormat("H", Locale.UK);
        String now = df.format(Calendar.getInstance().getTime());
        int currentHour = Integer.parseInt(now);
        if(currentHour < 8 || currentHour > 18)
            theme = R.style.DarkTheme;
        else
            theme = R.style.AppTheme;

        setTheme(theme);

        setContentView(R.layout.activity_list);

        database = Room.databaseBuilder(
                getApplicationContext(),
                ReservationDatabase.class,
                "reservation-list"
        ).build();

        SharedPreferences p = getPreferences(MODE_PRIVATE);
        boolean firstRun = p.getBoolean("PREFERENCE_FIRST_RUN", true);
        if(firstRun) addItems();
        p.edit().putBoolean("PREFERENCE_FIRST_RUN", false).apply();

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new VanAdapter(this, this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void addItems(){
        VanItem v = new VanItem("Fiat Ducato", "",
                VanItem.Category.MEDIUM, 800);
        addItem(v);

        v = new VanItem("Ford Transit Courier", "",
                VanItem.Category.SMALL, 500);
        addItem(v);

        v = new VanItem("Ford Transit Custom LWB", "",
                VanItem.Category.MEDIUM, 800);
        addItem(v);

        v = new VanItem("Mercedes Benz Sprinter", getString(R.string.furniture_van),
                VanItem.Category.LARGE, 1000);
        addItem(v);

        v = new VanItem("Peugeot Expert", "",
                VanItem.Category.MEDIUM, 800);
        addItem(v);

        v = new VanItem("Vauxhall Movan L3 H2", getString(R.string.furniture_van),
                VanItem.Category.LARGE, 900);
        addItem(v);

        v = new VanItem("Volkswagen Caddy Maxi", "",
                VanItem.Category.SMALL, 550);
        addItem(v);

        v = new VanItem("Volkswagen Transporter", getString(R.string.small_box_van),
                VanItem.Category.SMALL, 550);
        addItem(v);
    }

    private void addItem(final VanItem newItem){
        new AsyncTask<Void, Void, VanItem>() {

            @Override
            protected VanItem doInBackground(Void... voids) {
                newItem.id = database.vanItemDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(VanItem vanItem) {
                adapter.addItem(vanItem);
            }
        }.execute();
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<VanItem>>() {

            @Override
            protected List<VanItem> doInBackground(Void... voids) {
                return database.vanItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<VanItem> vanItems) {
                adapter.update(vanItems);
            }
        }.execute();
    }

    @Override
    public void onItemClicked(VanItem item) {
        Intent intent = new Intent();
        intent.setClass(ListActivity.this, TimePickerActivity.class);
        intent.putExtra(TimePickerActivity.VAN_ID, item);
        startActivity(intent);
    }
}
