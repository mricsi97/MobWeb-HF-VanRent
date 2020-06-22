package hu.bme.aut.vanrent.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReservationDao {
    @Query("SELECT * FROM reservation")
    List<Reservation> getAll();

    @Query("SELECT * FROM reservation ORDER BY starthour")
    List<Reservation> getAllSortByStartHour();

    @Insert
    long insert(Reservation reservations);

    @Update
    void update(Reservation reservation);

    @Delete
    void deleteItem(Reservation reservation);
}