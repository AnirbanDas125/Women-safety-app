package com.example.registeryourself;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MyLocation extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient locationProviderClient, locationProviderClient2;
    Button button_Emergency;
    Intent intent, intent2,intent3,intent4;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        getWindow().setFlags(WindowManager.LayoutParams.FLAGS_CHANGED,WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyLocation();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient2 = LocationServices.getFusedLocationProviderClient(this);
        button_Emergency = (Button) findViewById(R.id.button_Emergency);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.SEND_SMS)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();
                        getMyLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


        button_Emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationProviderClient2.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                       Location location = task.getResult();
                       if(location != null){
                           intent3 = getIntent();
                           intent4 = getIntent();
                           String contactNo = intent3.getStringExtra(AddContactActivity.Name_tag1);
                           String contactName = intent4.getStringExtra(AddContactActivity.Name_tag2);
                          String myLatitude =String.valueOf(location.getLatitude());
                          String myLongitude =String.valueOf(location.getLongitude());
                          String googleLink = "https://www.google.com/maps/search/?api=1&query="+myLatitude+"%2C"+myLongitude;
                          String msg = "Latitude = "+myLatitude+"\n"+"Longitude = "+myLongitude+"\n"+
                                  "Location Link Below"+"\n"+googleLink;
                           String phoneNo = contactNo;
                           SmsManager smsManager = SmsManager.getDefault();
                           smsManager.sendTextMessage(phoneNo,null,msg,null,null);
                           Toast.makeText(getApplicationContext(),"Location send to "+contactName,Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_addContact){
                intent2 = new Intent(MyLocation.this,AddContactActivity.class);
                startActivity(intent2);
        }
        if(item.getItemId() == R.id.item_logOut){
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(getApplicationContext(),logInActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = locationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                       try{
                               LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                               MarkerOptions marker = new MarkerOptions().position(latLng).title("Your Location");
                               googleMap.addMarker(marker);
                               googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                               googleMap.getUiSettings().setZoomControlsEnabled(true);
                               googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                               googleMap.getUiSettings().setCompassEnabled(true);

                       }catch (Exception e){
                           e.printStackTrace();
                           Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }
        });
    }
}