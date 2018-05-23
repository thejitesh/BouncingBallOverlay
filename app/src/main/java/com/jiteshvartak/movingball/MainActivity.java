package com.jiteshvartak.movingball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    BallView ballView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        ballView = findViewById(R.id.ballView);



    }

    @Override
    protected void onResume() {
        super.onResume();
        ballView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ballView.onPause();
    }
}
