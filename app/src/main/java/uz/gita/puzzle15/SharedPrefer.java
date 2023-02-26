package uz.gita.puzzle15;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefer {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String SHAREDPREFERENCES = "sharedPreference";
    private String BEST_SCORE = "best_score";
    private String BEST_TIME = "best_time";
    private static SharedPrefer instance;

    private SharedPrefer(Context context) {
        preferences = context.getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public int getBestMoves() {
        return preferences.getInt(BEST_SCORE, 0);
    }

    public void setBestMoves(int bestMoves) {
        editor.putInt(BEST_SCORE, bestMoves).apply();
    }

    public void setBestTime(int bestTime) {
        editor.putInt(BEST_TIME, bestTime).apply();
    }

    public int getBestTime() {
        return preferences.getInt(BEST_TIME, 0);
    }

    static void init(Context context) {
        if (instance == null) {
            instance = new SharedPrefer(context);
        }
    }

    static SharedPrefer getInstance(Context context) {
        if (instance == null) {
            return new SharedPrefer(context);
        }
        return instance;
    }
}
