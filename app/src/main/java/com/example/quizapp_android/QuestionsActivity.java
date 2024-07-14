package com.example.quizapp_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionsActivity extends AppCompatActivity{

    private TextView tv;
    private RadioGroup radio_g;
    private RadioButton rb1, rb2, rb3, rb4;

    private final String[] questions = {
            "Which method can be defined only once in a program?",
            "Which of these is not a bitwise operator?",
            "Which keyword is used by method to refer to the object that invoked it?",
            "Which of these keywords is used to define interfaces in Java?",
            "Which of these access specifiers can be used for an interface?",
            "Which of the following is correct way of importing an entire package ‘pkg’?",
            "What is the return type of Constructors?",
            "Which of the following package stores all the standard java classes?",
            "Which of these method of class String is used to compare two String objects for their equality?",
            "An expression involving byte, int, & literal numbers is promoted to which of these?"
    };
    private final String[] answers = {"main method", "<=", "this", "interface", "public", "import pkg.*", "None of the mentioned", "java", "equals()", "int"};
    private final String[] opt = {
            "finalize method", "main method", "static method", "private method",
            "&", "&=", "|=", "<=",
            "import", "this", "catch", "abstract",
            "Interface", "interface", "intf", "Intf",
            "public", "protected", "private", "All of the mentioned",
            "Import pkg.", "import pkg.*", "Import pkg.*", "import pkg.",
            "int", "float", "void", "None of the mentioned",
            "lang", "java", "util", "java.packages",
            "equals()", "Equals()", "isequal()", "Isequal()",
            "int", "long", "byte", "float"
    };
    private int flag = 0;
    public static int marks = 0, correct = 0, wrong = 0;

    private static final float SHAKE_THRESHOLD = 5.0f; // Adjust as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        TextView score = findViewById(R.id.textView4);
        TextView textView = findViewById(R.id.DispName);
        Intent intent = getIntent();
        String name = intent.getStringExtra("myname");

        if (name == null || name.trim().isEmpty())
            textView.setText("Hello User");
        else
            textView.setText("Hello " + name);

        Button submitbutton = findViewById(R.id.button3);
        Button quitbutton = findViewById(R.id.buttonquit);
        tv = findViewById(R.id.tvque);

        radio_g = findViewById(R.id.answersgrp);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);

        tv.setText(questions[flag]);
        rb1.setText(opt[0]);
        rb2.setText(opt[1]);
        rb3.setText(opt[2]);
        rb4.setText(opt[3]);

        submitbutton.setOnClickListener(v -> {
            if (radio_g.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton uans = findViewById(radio_g.getCheckedRadioButtonId());
            String ansText = uans.getText().toString();
            if (ansText.equals(answers[flag])) {
                correct++;
                Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            } else {
                wrong++;
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            }

            flag++;

            if (score != null)
                score.setText("" + correct);

            if (flag < questions.length) {
                tv.setText(questions[flag]);
                rb1.setText(opt[flag * 4]);
                rb2.setText(opt[flag * 4 + 1]);
                rb3.setText(opt[flag * 4 + 2]);
                rb4.setText(opt[flag * 4 + 3]);
            } else {
                marks = correct;
                Intent in = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(in);
            }
            radio_g.clearCheck();
        });

        quitbutton.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), ResultActivity.class);
            startActivity(intent1);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register accelerometer sensor listener when activity is resumed
        SensorService.getInstance(this).registerAccelerometerListener(this);
        SensorService.getInstance(this).registerLightSensorListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister accelerometer sensor listener when activity is paused
        SensorService.getInstance(this).unregisterAccelerometerListener();
        SensorService.getInstance(this).unregisterLightSensorListener();


    }

    void onShakeDetected(float xAcc) {
        if (xAcc > SHAKE_THRESHOLD) {
            // Tilted to the right (next question)
            showNextQuestion();
        } else if (xAcc < -SHAKE_THRESHOLD) {
            // Tilted to the left (previous question)
            showPreviousQuestion();
        }
    }

    public void showNextQuestion() {
        if (flag < questions.length - 1) {
            flag++;
            updateQuestion();
        } else {
            Toast.makeText(getApplicationContext(), "No more questions", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPreviousQuestion() {
        if (flag > 0) {
            flag--;
            updateQuestion();
        } else {
            Toast.makeText(getApplicationContext(), "You are at the first question", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuestion() {
        tv.setText(questions[flag]);
        rb1.setText(opt[flag * 4]);
        rb2.setText(opt[flag * 4 + 1]);
        rb3.setText(opt[flag * 4 + 2]);
        rb4.setText(opt[flag * 4 + 3]);
    }

}
