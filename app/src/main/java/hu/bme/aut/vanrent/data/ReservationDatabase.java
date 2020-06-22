package hu.bme.aut.vanrent.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {Reservation.class, VanItem.class},
        version = 1
)
@TypeConverters(value = {VanItem.Category.class})
public abstract class ReservationDatabase extends RoomDatabase {
    public abstract ReservationDao reservationDao();
    public abstract VanItemDao vanItemDao();
}