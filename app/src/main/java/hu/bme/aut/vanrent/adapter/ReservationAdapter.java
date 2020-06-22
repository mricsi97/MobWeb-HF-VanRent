package hu.bme.aut.vanrent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.vanrent.R;
import hu.bme.aut.vanrent.data.Reservation;
import hu.bme.aut.vanrent.data.TimeSlot;
import hu.bme.aut.vanrent.data.VanItem;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.TimeSlotViewHolder> {

    private Context context;

    private final List<Reservation> reservations;
    private final List<TimeSlot> timeSlots;

    private final int openHour = 9;
    private final int closeHour = 19;

    private TimeSlotClickedListener listener;

    public interface TimeSlotClickedListener{
        void onTimeSlotClicked(TimeSlot timeSlot);
    }

    public ReservationAdapter(Context context, TimeSlotClickedListener listener){
        this.listener = listener;
        this.context = context;
        this.timeSlots = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public void update(List<Reservation> reservationList) {
        this.reservations.clear();
        this.reservations.addAll(reservationList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.time_slot, parent, false);
        return new TimeSlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlots.get(position);
        holder.timeSlotButton.setText(context.getString(R.string.time_interval, timeSlot.startHour, timeSlot.endHour));

        holder.timeSlot = timeSlot;
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void addReservation(Reservation res){
        reservations.add(res);
        notifyItemInserted(reservations.size() - 1);
    }

    public int calculateTimeSlots(int year, int month, int day, VanItem van){
        List<Reservation> relevantReservations = new ArrayList<>();
        for(int i = 0; i < reservations.size(); i++){
            Reservation current = reservations.get(i);
            if(current.year == year && current.month == month && current.day == day && current.vanId == van.id)
                relevantReservations.add(current);
        }

        int startHour = openHour;

        for(int i = 0; i < relevantReservations.size(); i++){
            Reservation current = relevantReservations.get(i);
            if(current.startHour > startHour) {
                timeSlots.add(new TimeSlot(startHour, current.startHour));
            }
            startHour = current.endHour;
        }
        if(startHour < closeHour) timeSlots.add(new TimeSlot(startHour, closeHour));

        return timeSlots.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {

        Button timeSlotButton;

        TimeSlot timeSlot;

        TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlotButton = itemView.findViewById(R.id.btnTimeSlot);

            timeSlotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onTimeSlotClicked(timeSlot);
                    }
                }
            });
        }
    }
}
