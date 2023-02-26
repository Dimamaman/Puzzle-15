package uz.gita.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "result_one")
public class Result {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "moves")
    public int moves;

    public Result(int id, String time, int score) {
        this.id = id;
        this.time = time;
        this.moves = score;
    }

    public Result() {

    }
}
