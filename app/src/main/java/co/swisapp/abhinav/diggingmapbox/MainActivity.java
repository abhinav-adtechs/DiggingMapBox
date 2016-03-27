package co.swisapp.abhinav.diggingmapbox;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMarkerClickListener, MapboxMap.OnCameraChangeListener{

    private MapView mapView = null ;
    private MapboxMap mapboxMap ;


    private Intent videoPlayIntent ;

    private static final int PERMISSIONS_LOCATION = 0;

    private MarkersList markersList ;
    private List<MarkersList.MarkerData> markers ;

    CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(12.9693, 79.1559)) // set the camera's center position
            .zoom(16)
            .tilt(24)
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchData(new LatLng(12.9693, 79.1559));
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

/*
        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mIconDrawable = ContextCompat.getDrawable(this, R.drawable.imguser);
        iconUser = mIconFactory.fromDrawable(mIconDrawable);
*/



        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(MapboxMap map) {
        mapboxMap =  map ;

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        } else {
            mapboxMap.setMyLocationEnabled(true);
        }


        mapboxMap.setStyle(Style.EMERALD);
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng center = mapboxMap.getCameraPosition().target ;
        Log.d("testing", "onMapReady() called with: " + "center" + center);


        mapboxMap.addMarker(new MarkerOptions()
                .title("Jordiie")
                .snippet("an hour ago")
                .position(new LatLng(12.9695, 79.1559)));

        mapboxMap.setOnCameraChangeListener(this);
        mapboxMap.setOnMarkerClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getBaseContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getBaseContext().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapboxMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        LatLng test = marker.getPosition() ;
        Log.d("testing", "onMarkerClick: " + test );
        return false;
    }

    @Override
    public void onCameraChange(CameraPosition position) {
        Log.d("Position Testing", "onCameraChange() called with: " + "position = [" + position + "]");
        fetchData(position.target);
    }

    private void fetchData(final LatLng centerPosition) {

        String markersUrl = "api.swisapp.co/ ";
        StringRequest markersRequest = new StringRequest(Request.Method.POST, markersUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponse(response) ;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>() ;
                params.put("centerPosition", centerPosition.toString()) ;
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headers = new HashMap() ;
                headers.put("auth-key", "somethingsomething") ;
                return headers ;
            }
        };

        AppController.getInstance().addToRequestQueue(markersRequest);
    }

    private void handleResponse(String response) {


    }

}


