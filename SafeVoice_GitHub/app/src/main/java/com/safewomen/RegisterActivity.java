package com.safewomen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText emailInput, passInput, trustInput;
    Button registerBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailInput = findViewById(R.id.email);
        passInput = findViewById(R.id.password);
        trustInput = findViewById(R.id.trust);
        registerBtn = findViewById(R.id.registerBtn);
        db = new DatabaseHelper(this);

        registerBtn.setOnClickListener(v -> {
            String e = emailInput.getText().toString();
            String p = passInput.getText().toString();
            String t = trustInput.getText().toString();
            if (e.isEmpty() || p.isEmpty() || t.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = db.insertUser(e, p, t);
            if (ok) {
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
