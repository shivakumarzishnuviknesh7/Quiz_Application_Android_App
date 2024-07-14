package com.example.quizapp_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nametext;
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            }
        }
        nametext = findViewById(R.id.editName);
        Button startbutton = findViewById(R.id.button);
        Button aboutbutton = findViewById(R.id.button2);

        startbutton.setOnClickListener(v -> {
            String name = nametext.getText().toString();
            Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
            intent.putExtra("myname", name);
            startActivity(intent);
        });

        aboutbutton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Toast.makeText(this, "Permission denied to write system settings", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorService.getInstance(this).registerLightSensorListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SensorService.getInstance(this).unregisterLightSensorListener();


    }

}