package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.barcodescanner.java.ScancodeActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonScan,buttonTake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonScan=findViewById(R.id.buttonScancode);
        //buttonTake=findViewById(R.id.buttonTakePicture);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(MainActivity.this, ScancodeActivity.class));
            }
        });

//        buttonTake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,TakeImageActivity.class));
//            }
//        });
    }
}
