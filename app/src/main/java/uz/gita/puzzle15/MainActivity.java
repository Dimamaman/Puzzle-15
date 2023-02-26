package uz.gita.puzzle15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        findViewById(R.id.ln_start).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,GameActivity.class));
        });

        findViewById(R.id.ln_statistics).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,StatisticsActivity.class));
        });

        findViewById(R.id.ln_info).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,InfoActivity.class));
        });
    }
}