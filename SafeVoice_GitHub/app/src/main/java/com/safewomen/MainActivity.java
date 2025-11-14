package com.safewomen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Request microphone and phone permission
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION};
        boolean ok = true;
        for (String p: perms) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                ok = false;
            }
        }
        if (!ok) {
            ActivityCompat.requestPermissions(this, perms, 100);
        }

        start = findViewById(R.id.startServiceBtn);
        start.setOnClickListener(v -> startService(new Intent(MainActivity.this, VoiceService.class)));
    }
}
