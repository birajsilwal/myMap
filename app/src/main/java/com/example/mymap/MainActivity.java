 package com.example.mymap;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

 public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
    }

     @Override
     protected void onStart() {
         super.onStart();
         mapView.onStart();
     }

     @Override
     protected void onResume() {
         super.onResume();
         mapView.onPause();
     }

     @Override
     protected void onPause() {
         super.onPause();
         mapView.onPause();
     }

     @Override
     protected void onStop() {
         super.onStop();
         mapView.onStop();
     }

     @Override
     public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
         super.onSaveInstanceState(outState, outPersistentState);
         mapView.onSaveInstanceState(outState);
     }

     @Override
     public void onLowMemory() {
         super.onLowMemory();
         mapView.onLowMemory();
     }

     @Override
     protected void onDestroy() {
         super.onDestroy();
         mapView.onDestroy();
     }
 }

