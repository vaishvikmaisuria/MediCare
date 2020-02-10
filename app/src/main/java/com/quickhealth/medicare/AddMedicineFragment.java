package com.quickhealth.medicare;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.quickhealth.medicare.webservice.RetrofitClientInstance;
import com.quickhealth.medicare.webservice.UserProfile;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddMedicineFragment  extends Fragment implements View.OnClickListener {
    private static final int REQUEST_LOCATION = 1;
    Button buttonLocation;
    Button buttonMedicine;
    private ProgressBar spinner;
    LocationManager locationManager;
    String lattitude, longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        View v = inflater.inflate(R.layout.activity_alert, container, false);
        spinner = (ProgressBar) v.findViewById(R.id.spin);
        buttonLocation = (Button) v.findViewById(R.id.button_alert);
        buttonMedicine = (Button) v.findViewById(R.id.button_medicine);

        buttonLocation.setOnClickListener(this);
        buttonMedicine.setOnClickListener(this);

        return v;



    }

    private void getLocation() throws JSONException {
        double latti = 0;
        double longi = 0;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            spinner.setVisibility(View.INVISIBLE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Log.d("lattitude0", lattitude);
                Log.d("lattitude0", longitude);

            } else if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Log.d("lattitude1", lattitude);
                Log.d("lattitude1", longitude);

            } else if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Log.d("lattitude2", lattitude);
                Log.d("lattitude2", longitude);

            } else {

                Toast.makeText(getActivity(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }

            // Get current logged in user's id
            String id = CurrentUser.getUserID();
            // Obtain an instance of Retrofit by calling the static method.
            Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
            // The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
            UserProfile usersLocation = retrofit.create(UserProfile.class);
            // Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
            Call<String> call;
            String token = CurrentUser.getUserToken();
            call = usersLocation.setuserLocation(id, lattitude, longitude, token);

            // This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/

                }
            });

            CurrentUser.setUserLattitude(lattitude);
            CurrentUser.setUserLongitude(longitude);

        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_alert: {
                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        spinner.setVisibility(View.VISIBLE);
                    }

                    public void onFinish() {
                        spinner.setVisibility(View.INVISIBLE);
                    }
                }.start();

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        getLocation();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                Intent intent = new Intent(getActivity(), Medical_History.class);
//                startActivity(intent);
                break;
            }
            case R.id.button_medicine: {
                Intent intent = new Intent(getActivity(), personal_info.class);
                startActivity(intent);
                break;
            }

        }

        //        Intent intent2 = new Intent(getActivity().getApplicationContext(),SOS_Service.class);
        //        ContextCompat.startForegroundService(getActivity(), intent2);
        //        Intent intent = new Intent(getActivity(), additional_Details.class);
        //        startActivity(intent);
    }
}
