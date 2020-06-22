package hu.bme.aut.vanrent.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(tableName = "vanitem")
public class VanItem implements Parcelable{
    public enum Category {
        SMALL, MEDIUM, LARGE;

        @TypeConverter
        public static Category getByOrdinal(int ordinal) {
            Category ret = null;
            for (Category cat : Category.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break;
                }
            }
            return ret;
        }

        @TypeConverter
        public static int toInt(Category category) {
            return category.ordinal();
        }
    }

    public VanItem(String name, String description, Category size, int price){
        this.name = name;
        this.description = description;
        this.size = size;
        this.price = price;
    }

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "size")
    public Category size;

    @ColumnInfo(name="price")
    public int price;

    protected VanItem(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        name = in.readString();
        description = in.readString();
        size = (Category) in.readValue(Category.class.getClassLoader());
        price = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeValue(size);
        dest.writeInt(price);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VanItem> CREATOR = new Parcelable.Creator<VanItem>() {
        @Override
        public VanItem createFromParcel(Parcel in) {
            return new VanItem(in);
        }

        @Override
        public VanItem[] newArray(int size) {
            return new VanItem[size];
        }
    };
}