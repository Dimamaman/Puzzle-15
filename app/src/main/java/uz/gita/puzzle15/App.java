package uz.gita.puzzle15;

import android.app.Application;

import uz.gita.room.ResultDatabase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefer.init(this);

        ResultDatabase.init(this);
    }
}
