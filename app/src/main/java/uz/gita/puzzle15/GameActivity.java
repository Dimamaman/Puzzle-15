package uz.gita.puzzle15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kotlin.text.StringsKt;
import uz.gita.room.ResultDao;
import uz.gita.room.ResultDatabase;

public class GameActivity extends AppCompatActivity {
    private TextView textScore;
    private int bestScore;
    private TextView tvBest;
    private Chronometer textTime;
    private boolean isFirstClick = true;
    private Button[][] buttons;
    private List<Integer> numbers;
    private Coordinate emptySpace;
    private int score;

    private boolean soundButtonStatus = true;
    private MediaPlayer mediaPlayer;
    private MediaPlayer clickMP;
    private SharedPrefer sharedPrefer;

    private ResultDao resultDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        resultDao = ResultDatabase.getInstance(this).resultDao();
//        resultDao = ResultDatabase.getInstance(this).resultDao();
        sharedPrefer = SharedPrefer.getInstance(this);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gta_1);
        mediaPlayer.setLooping(true);


        loadView();
        getBestScore();
        loadData();
        dataToView();

        setSoundButtonStatus();
        findViewById(R.id.iv_sound).setOnClickListener(view -> onSoundButtonClick((FrameLayout) view));
    }

    private void loadView() {
        textScore = findViewById(R.id.tv_score);
        textTime = findViewById(R.id.tv_time);
        tvBest = findViewById(R.id.tv_best);


        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.iv_restart).setOnClickListener(v -> restart());
        findViewById(R.id.iv_share).setOnClickListener(view -> shareStatistics());

        final ViewGroup group = findViewById(R.id.container);
        final int count = group.getChildCount();

        buttons = new Button[4][4];

        for (int i = 0; i < count; i++) {
            final View view = group.getChildAt(i);
            final Button button = (Button) view;
            final int y = i / 4;
            final int x = i % 4;
            button.setOnClickListener(v -> onItemClick(button, x, y));
            buttons[y][x] = button;
        }
        emptySpace = new Coordinate(3, 3);
    }

    private void loadData() {
        numbers = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            numbers.add(i);
        }
    }

    private void dataToView() {
        score = 0;
        textScore.setText("0");

        textTime.setText("00:00");
        isFirstClick = true;
        textTime.stop();


        Collections.shuffle(numbers);
        buttons[emptySpace.y][emptySpace.x].setBackgroundResource(R.drawable.not_pressed_icon);

        emptySpace.x = 3;
        emptySpace.y = 3;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int index = 4 * i + j;
                if (index < 15) {
                    int number = numbers.get(index);
                    buttons[i][j].setText(String.valueOf(number));
                } else {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackgroundResource(R.drawable.pressed_icon);
                }
            }
        }
    }

    private void restart() {
        dataToView();
    }

    private void onItemClick(Button button, int x, int y) {
        playClickSound();

        final int dx = Math.abs(emptySpace.x - x);
        final int dy = Math.abs(emptySpace.y - y);
        if (dx + dy == 1) {
            if (isFirstClick) {
                textTime.setBase(SystemClock.elapsedRealtime());
                textTime.start();
                isFirstClick = false;
            }
            textScore.setText(String.valueOf(++score));
            final String text = button.getText().toString();
            button.setText("");
            button.setBackgroundResource(R.drawable.pressed_icon);

            final Button temp = buttons[emptySpace.y][emptySpace.x];
            temp.setText(text);
            temp.setBackgroundResource(R.drawable.not_pressed_icon);

            emptySpace.x = x;
            emptySpace.y = y;

            if (isWin()) {
                saveBestScore();
                saveBestTime();
//                resultDao.insertResult(new Result(0,textTime.getText().toString(),score));
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
                restart();
            }
        }
    }

    private boolean isWin() {
        if (emptySpace.x != 3 || emptySpace.y != 3) return false;

        for (int i = 0; i < 15; i++) {
            final int y = i / 4;
            final int x = i % 4;
            final String text = buttons[y][x].getText().toString();
            if (!text.equals(String.valueOf(i + 1))) return false;
        }

        return true;
    }

    private void shareStatistics() {
        try {
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType("text/plain");
            shareIntent.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
            String message = String.valueOf(bestScore);
            String shareMessage = StringsKt.trim((CharSequence) message) + "\n";
            shareMessage = StringsKt.trimIndent(shareMessage);
            shareIntent.putExtra("android.intent.extra.TEXT", shareMessage);
            this.startActivity(Intent.createChooser(shareIntent, (CharSequence) "Share This App"));
        } catch (Exception e) {
            Log.d("TTT", e.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void getBestScore() {
        if (sharedPrefer.getBestMoves() != -1) {
            bestScore = sharedPrefer.getBestMoves();
            tvBest.setText("" + bestScore);
        }
    }

    @SuppressLint("SetTextI18n")
    private void saveBestScore() {
        if (tvBest.getText().toString().equals("0")) {
            sharedPrefer.setBestMoves(score);
            bestScore = sharedPrefer.getBestMoves();
        } else {
            int min = Math.min(score,sharedPrefer.getBestMoves());
            sharedPrefer.setBestMoves(min);
        }
        tvBest.setText("" + bestScore);
    }

    private void saveBestTime() {
        String currentTime = textTime.getText().toString();
        long time = 0L;
        List<String> list;
        list = Arrays.asList(currentTime.split(":"));

        int x = Integer.parseInt(list.get(0));
        int y = Integer.parseInt(list.get(1));

        if (list.size() == 2) {
            time += x * 60L + y;
        }

        if (sharedPrefer.getBestTime() == -1) {
            sharedPrefer.setBestTime(Integer.parseInt(currentTime));
        } else {
            sharedPrefer.setBestTime((int) Math.min(sharedPrefer.getBestTime(),time));
        }

    }

    private void setSoundButtonStatus() {
        ImageView ivSound = findViewById(R.id.iv_sound1);
        if (!soundButtonStatus) {
            ivSound.setImageResource(R.drawable.ic_volume_off);
            pause();
        } else {
            ivSound.setImageResource(R.drawable.ic_volume_up);
            play();
        }
    }

    private void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playClickSound() {
        if (clickMP != null) {
            clickMP.release();
        }
        clickMP = MediaPlayer.create(this, R.raw.single);
        clickMP.start();
    }

    private void onSoundButtonClick(FrameLayout view) {
        AppCompatImageView ivSound = view.getChildAt(0).findViewById(R.id.iv_sound1);

        if (soundButtonStatus) {
            soundButtonStatus = false;
            ivSound.setImageResource(R.drawable.ic_volume_off);
            mediaPlayer.pause();
        } else {
            soundButtonStatus = true;
            ivSound.setImageResource(R.drawable.ic_volume_up);
            mediaPlayer.start();
        }
    }
}