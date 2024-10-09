package com.example.praka3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ClickerActivity extends AppCompatActivity {

    private int score = 0;
    private TextView scoreView;
    private ImageView hamsterImage;
    private ImageView changeThemeImage;

    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String KEY_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme(); // Устанавливаем тему при запуске
        setContentView(R.layout.activity_clicker);

        scoreView = findViewById(R.id.scoreView);
        hamsterImage = findViewById(R.id.hamsterImage);
        changeThemeImage = findViewById(R.id.changeThemeImage);

        hamsterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;
                scoreView.setText("Очки: " + score);
            }
        });

        // Устанавливаем слушатель для смены темы
        changeThemeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppTheme(); // Меняем тему при клике
            }
        });

        // Обновляем изображение луны в зависимости от темы
        updateMoonImage();
    }

    // Метод для установки темы из SharedPreferences
    private void setAppTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);
        if (darkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }
    }

    // Меняем тему и сохраняем выбор в SharedPreferences
    private void changeAppTheme() {
        saveGameState(); // Сохраняем состояние перед сменой темы
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_THEME, !darkTheme);
        editor.apply();

        // Перезагрузка активности для применения новой темы
        recreate();
    }

    // Сохранение текущего состояния игры
    private void saveGameState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("score", score); // Сохраняем текущий счет
        editor.apply();
    }

    // Восстанавливаем состояние игры при создании активности
    private void loadGameState() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        score = prefs.getInt("score", 0); // Восстанавливаем текущий счет
        scoreView.setText("Очки: " + score); // Обновляем отображение счета
    }

    // Обновляем изображение луны в зависимости от текущей темы
    private void updateMoonImage() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_THEME, false);

        if (darkTheme) {
            changeThemeImage.setImageResource(R.drawable.moon_white); // Белая луна для темной темы
        } else {
            changeThemeImage.setImageResource(R.drawable.moon_black); // Черная луна для светлой темы
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGameState(); // Восстанавливаем состояние игры при возобновлении активности
        updateMoonImage(); // Обновляем изображение луны при возврате к активности
    }
}
