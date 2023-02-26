package uz.gita.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultDao {
    @Insert
    void insertResult(Result result);

    @Query("SELECT * FROM result_one")
    List<Result> getAllResults();

}
