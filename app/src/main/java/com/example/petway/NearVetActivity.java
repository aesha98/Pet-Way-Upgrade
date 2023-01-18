package com.example.petway;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.petway.Adapter.VetAdapter;
import com.example.petway.Model.Vet;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearVetActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private final List<Vet> vetList = new ArrayList<>();
    private VetAdapter adapter;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    public ActivityResultLauncher<String[]> locationPermissionRequest;
    public String idpalce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_vet);
        String API_KEY = getString(R.string.API_KEY);
        Places.initialize(getApplicationContext(), API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        RecyclerView recyclerView = findViewById(R.id.recyckerview_vet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VetAdapter(vetList, this);
        recyclerView.setAdapter(adapter);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        permissionRequestinit();
        requestpermission();
        getLastknownLocation();
    }

    public void permissionRequestinit() {
        locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                fineLocationGranted = result.getOrDefault(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                            }
                            Boolean coarseLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                coarseLocationGranted = result.getOrDefault(
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            }
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                Toast.makeText(this, "Location Access : Granted", Toast.LENGTH_SHORT).show();
                                locationPermissionGranted = true;
                                getLastknownLocation();

                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                                Toast.makeText(NearVetActivity.this, "Location Access : Denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    public void requestpermission() {
        if (ContextCompat.checkSelfPermission(NearVetActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationPermissionGranted = true;


        } else if (ActivityCompat.shouldShowRequestPermissionRationale(NearVetActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionDeniedAlert();
        } else {
            locationPermissionRequest.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    public void permissionDeniedAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(NearVetActivity.this);

        dialog.setTitle("Requires Location Permission");
        dialog.setMessage("This app need your location permission to continue");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                locationPermissionRequest.launch(new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION});
            }
        });
        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(NearVetActivity.this, "Sorry,the app wont run until permission is given. Consent is very important you know.", Toast.LENGTH_SHORT).show());

        dialog.show();
    }

    @SuppressLint("MissingPermission")
    public void getLastknownLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Use the Places API to search for nearby places
                    searchNearbyPlaces(latitude, longitude);
                }
            }
        });

    }


    private void searchNearbyPlaces(double latitude, double longitude) {
        String apiKey = String.valueOf(R.string.API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        String query = "veterinary clinic near me";

        final double HEADING_NORTH_EAST = 45;
        final double HEADING_SOUTH_WEST = 215;
        final double diagonalBoundsSize = 5000; // 1km

        LatLng centre = new LatLng(85, -180);

        LatLng northEast = SphericalUtil.computeOffset(centre, diagonalBoundsSize / 2, HEADING_NORTH_EAST);
        LatLng southWest = SphericalUtil.computeOffset(centre, diagonalBoundsSize / 2, HEADING_SOUTH_WEST);
        LatLngBounds bounds = new LatLngBounds(southWest, northEast);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(RectangularBounds.newInstance(bounds))
                .setCountries("MY")
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                String placeId = prediction.getPlaceId();
                String name = prediction.getPrimaryText(null).toString();
                String address = prediction.getSecondaryText(null).toString();
                Log.i("placeID", placeId);
                Log.i("name", name);
                Log.i("address", address);

                getVetInfo(placeId, placesClient, name, address);

            }
        }).addOnFailureListener(e -> Log.d("Places API", "Failed to retrieve autocomplete predictions: " + e.getMessage()));
    }

        private void getVetInfo (String placeid, PlacesClient placesClient, String name, String address)
        {
            List<Place.Field> placeFields = Arrays.asList(Place.Field.PHONE_NUMBER);
            FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeid, placeFields);

            placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                String phoneNumber = place.getPhoneNumber();
               // place.getUserRatingsTotal();
                //Add the places to your list
                Vet vet = new Vet(name, address, place.getPhoneNumber());
                vetList.add(vet);
                Log.i("I", "Place phone number: " + phoneNumber);
                adapter.notifyDataSetChanged();

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                }
            });
        }
    }

