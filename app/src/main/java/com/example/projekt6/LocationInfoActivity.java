package com.example.projekt6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LocationInfoActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView txtInfoLatitude;
    private TextView txtInfoLongitude;
    private TextView txtInfoDescription;
    private TextView txtInfoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        initControls();

        String name;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            db = new DatabaseHelper(this);
            DBLocation location = db.getLocation(name);

            txtInfoLatitude.setText(String.format("%.4f", location.getLatitude()));
            txtInfoLongitude.setText(String.format("%.4f", location.getLongitude()));
            txtInfoName.setText(location.getName());
            txtInfoDescription.setText(location.getDescription());
        } else {
            message("Błąd podczas przekazywania id.");
            return;
        }

    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        txtInfoLatitude = findViewById(R.id.txtInfoLatitude);
        txtInfoLongitude = findViewById(R.id.txtInfoLongitude);
        txtInfoDescription = findViewById(R.id.txtInfoDescription);
        txtInfoName = findViewById(R.id.txtInfoName);
    }
}