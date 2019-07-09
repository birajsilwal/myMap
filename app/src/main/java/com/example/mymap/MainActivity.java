 package com.example.mymap;

import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;

 public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

     @Override
     public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        enableLocation();
     }

     //true or false whether or not the user has granted location permission
     private void enableLocation(){
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            initializeLocationLayer();
        }
        //otherwise it will ask for a permission for location
        else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
     }

     @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        //gets last location if any
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null){
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        }
        else {
            locationEngine.addLocationEngineListener(this);
        }
    }

     @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition (Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0));
    }

     @SuppressWarnings("MissingPermission")
     @Override
     public void onConnected() {
        locationEngine.requestLocationUpdates();
     }

     //if location changes then we need to change origin location and change camera position to that new location.
     @Override
     public void onLocationChanged(Location location) {
        if (location != null ) {
            originLocation = location;
            setCameraPosition(location);
        }
     }

     @Override
     public void onExplanationNeeded(List<String> permissionsToExplain) {
        // present toast or dialog to explain why user need to grant access to the location
     }

     @Override
     public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

     }

     @SuppressWarnings("MissingPermission")
     @Override
     protected void onStart() {
         super.onStart();
         if (locationEngine != null) {
             locationEngine.requestLocationUpdates();
         }
         if (locationLayerPlugin != null) {
             locationLayerPlugin.onStart();
         }
         mapView.onStart();
     }

     @Override
     protected void onResume() {
         super.onResume();
         mapView.onResume();
     }

     @Override
     protected void onPause() {
         super.onPause();
         mapView.onPause();
     }

     @Override
     protected void onStop() {
         super.onStop();
         if (locationEngine != null) {
             locationEngine.removeLocationUpdates();
         }
         if (locationLayerPlugin != null){
             locationLayerPlugin.onStop();
         }
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
         if (locationEngine != null) {
             locationEngine.deactivate();
         }
         mapView.onDestroy();
     }

 }

