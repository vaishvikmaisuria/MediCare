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

import com.github.nkzawa.socketio.client.Socket;
import com.quickhealth.medicare.httpRequestHelpers.httpPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class ContactFragment extends Fragment implements View.OnClickListener {

    private ProgressBar spinner;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_alert, container, false);

        spinner = (ProgressBar) v.findViewById(R.id.spin);



        return v;
    }

    public void onClick(View view) {



//        Intent intent2 = new Intent(getActivity().getApplicationContext(),SOS_Service.class);
//        ContextCompat.startForegroundService(getActivity(), intent2);
//        Intent intent = new Intent(getActivity(), additional_Details.class);
//        startActivity(intent);

    }



}