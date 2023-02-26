package uz.gita.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Result.class, version = 1)
public abstract class ResultDatabase extends RoomDatabase {

    private static final String DB_NAME = "result_one.db";

    private static ResultDatabase instance;

    public static synchronized ResultDatabase init(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    ResultDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static ResultDatabase getInstance(Context context) {
        return instance;
    }

    public abstract ResultDao resultDao();
}
