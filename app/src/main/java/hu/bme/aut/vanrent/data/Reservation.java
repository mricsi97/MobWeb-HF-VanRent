package hu.bme.aut.vanrent.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservation",
        indices = {@Index("vanId")},
        foreignKeys = @ForeignKey(entity = VanItem.class,
        parentColumns = "id",
        childColumns = "vanId",
        onDelete = ForeignKey.CASCADE))
public class Reservation {

    public Reservation(int year, int month, int day, int startHour, int endHour, Long vanId){
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.vanId = vanId;
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "month")
    public int month;

    @ColumnInfo(name = "day")
    public int day;

    @ColumnInfo(name = "starthour")
    public int startHour;

    @ColumnInfo(name = "endhour")
    public int endHour;

    @ColumnInfo(name = "vanId")
    public Long vanId;
}
