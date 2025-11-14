package com.safewomen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class EmergencyActivity extends AppCompatActivity {

    TextView countdownText;
    CountDownTimer timer;
    DatabaseHelper db;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        countdownText = findViewById(R.id.timer);
        db = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long ms) {
                countdownText.setText("Calling in " + (ms/1000) + "s\nTap SAFE if you're OK");
            }

            @Override
            public void onFinish() {
                String number = db.getTrustedNumber();
                if (number == null || number.isEmpty()) return;
                // Send SMS with last known location (best effort)
                sendLocationSms(number);
                // Initiate call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
                startActivity(callIntent);
            }
        };
        timer.start();
    }

    public void onSafeClicked(View v) {
        timer.cancel();
        finish();
    }

    private void sendLocationSms(String number) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(loc -> {
            String msg = "I'm in trouble. Please help. Last known location: ";
            if (loc != null) {
                msg += "https://www.google.com/maps?q=" + loc.getLatitude() + "," + loc.getLongitude();
            } else {
                msg += "Location not available";
            }
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, msg, null, null);
                Toast.makeText(EmergencyActivity.this, "SMS sent", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
