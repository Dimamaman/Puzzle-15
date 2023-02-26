package uz.gita.puzzle15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uz.gita.room.Result;
import uz.gita.room.ResultDao;
import uz.gita.room.ResultDatabase;

public class StatisticsActivity extends AppCompatActivity {

    private TextView bestScore;
    private TextView bestTime;

    private ResultDao resultDao;

    private TextView firstScoreTv;
    private TextView secondScoreTv;
    private TextView thirdScoreTv;

    private TextView firstTime;
    private TextView secondTime;
    private TextView thirdTime;

    private List<Result> list = new ArrayList<>();
    private SharedPrefer sharedPrefer = SharedPrefer.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        resultDao = ResultDatabase.init(this).resultDao();
        list.addAll(resultDao.getAllResults());

        findViewById(R.id.iv_back).setOnClickListener(view -> {
            finish();
        });

        bestScore = findViewById(R.id.best_score);
        bestTime = findViewById(R.id.best_time);
        findViews();
        loadResults();
        loadTop3Results();

    }

    private void findViews() {
        firstScoreTv = findViewById(R.id.firs_score_tv);
        secondScoreTv = findViewById(R.id.second_score_tv);
        thirdScoreTv = findViewById(R.id.third_score_tv);

        firstTime = findViewById(R.id.time_first);
        secondTime = findViewById(R.id.time_second);
        thirdTime = findViewById(R.id.time_third);
    }

    private void loadResults() {
        int score = SharedPrefer.getInstance(this).getBestMoves();
        bestScore.setText("" + score);

        int time = SharedPrefer.getInstance(this).getBestTime();
        bestTime.setText("" + time);
    }

    @SuppressLint("SetTextI18n")
    private void loadTop3Results() {
        list.sort(Comparator.comparingInt(result -> result.moves));

        if (list.size() >= 3) {
            firstScoreTv.setText("" + list.get(0).moves);
            secondScoreTv.setText("" + list.get(1).moves);
            thirdScoreTv.setText("-");

            firstTime.setText("" + list.get(0).time);
            secondTime.setText("" + list.get(1).time);
            thirdTime.setText("" + list.get(3).time);
        }


        switch (list.size()) {
            case 1: {
                firstScoreTv.setText("" + list.get(0).moves);
                secondScoreTv.setText("-");
                thirdScoreTv.setText("-");

                firstTime.setText("" + list.get(0).time);
                secondTime.setText("");
                thirdTime.setText("");
            }

            case 2: {
                firstScoreTv.setText("" + list.get(0).moves);
                secondScoreTv.setText("" + list.get(1).moves);
                thirdScoreTv.setText("-");

                firstTime.setText("" + list.get(0).time);
                secondTime.setText("" + list.get(1).time);
                thirdTime.setText("");
            }
        }
    }
}