package com.example.projekt6;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recLocations;
    private SeekBar seekRadius;
    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtRadius;

    private DatabaseHelper db;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = location -> {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            txtLatitude.setText(String.format("%.4f", latitude));
            txtLongitude.setText(String.format("%.4f", longitude));

            currentLocation = location;
        };

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        db = new DatabaseHelper(this);
        List<DBLocation> allLocations = db.getAllLocations();

        MainActivity context = this;

        seekRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onProgressChanged(SeekBar seekBar, int radiusKm, boolean fromUser) {
                txtRadius.setText(String.format("%d", radiusKm) + " km");

                if(currentLocation == null) {
                    message("Jeszcze nie wczytano lokalizacji.");
                    return;
                }

                List<DBLocation> locationsInRadius = allLocations.stream().filter(location -> {
                    Location dbLocation = new Location("");
                    dbLocation.setLatitude(location.getLatitude());
                    dbLocation.setLongitude(location.getLongitude());
                    float distKm = currentLocation.distanceTo(dbLocation) / 1000.0f;
                    return distKm < radiusKm;
                }).collect(Collectors.toList());

                LocationAdapter locationAdapter = new LocationAdapter(locationsInRadius, context);
                recLocations.setAdapter(locationAdapter);
                recLocations.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void message(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void initControls() {
        recLocations = findViewById(R.id.recLocations);
        seekRadius = findViewById(R.id.seekRadius);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);
        txtRadius = findViewById(R.id.txtRadius);
    }
}