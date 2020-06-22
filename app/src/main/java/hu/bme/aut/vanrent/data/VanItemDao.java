package hu.bme.aut.vanrent.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VanItemDao {
    @Query("SELECT * FROM vanitem")
    List<VanItem> getAll();

    @Insert
    long insert(VanItem vanItems);

    @Update
    void update(VanItem vanItem);

    @Delete
    void deleteItem(VanItem vanItem);
}