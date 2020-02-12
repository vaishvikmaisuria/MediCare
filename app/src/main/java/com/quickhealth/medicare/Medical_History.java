package com.quickhealth.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;


import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.CompositeDisposable;

public class Medical_History extends AppCompatActivity {
    EditText otherText;
    JSONObject change = initJSON();
    JSONObject send;
    private  final CompositeDisposable compositeDisposable = new CompositeDisposable();
    Button sender;

    public Medical_History() throws JSONException {
    }

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);


        otherText = (EditText)findViewById(R.id.TextInputLayoutmine);


        sender = (Button) findViewById(R.id.button12);

        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Send to server update the user medical history info
//                    change.put("other", otherText.getEditText().getText().toString());
                    send = new JSONObject();

                    send.put("username",CurrentUser.getUserID());
                    send.put("medicalInfo",change);

//                    "https://quick-health.herokuapp.com/user/updateInfo"
                    Snackbar.make(view.getRootView(), "Updated", Snackbar.LENGTH_LONG).show();


                }catch (Exception e) {
                    Log.d("Error", e.getLocalizedMessage());
                }

            }
        });
    }

    public void onCheckboxClicked(View view) throws JSONException {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.checkBox2:
                if (checked) {
                    change.put("highCholesterol",true);
                }
                break;

            case R.id.checkBox3:
                if (checked) {
                    change.put("kidneyDisease",true);
                }
                break;
            case R.id.checkBox4:
                if (checked) {
                    change.put("thyroidProblems",true);
                }
                break;
            case R.id.checkBox5:
                if (checked) {
                    change.put("jointReplacement",true);
                }
                break;
            case R.id.checkBox6:
                if (checked) {
                    change.put("lungDisease",true);
                }
                break;
            case R.id.checkBox7:
                if (checked) {
                    change.put("stroke",true);
                }
                break;
            case R.id.checkBox8:
                if (checked) {
                    change.put("asthmas",true);
                }
                break;
            case R.id.checkBox9:
                if (checked) {
                    change.put("heartProblem",true);
                }
                break;
        }
    }


    public JSONObject initJSON() throws  JSONException{
        JSONObject x = new JSONObject();
        x.put("highBloodPressure",false);
        x.put("highCholesterol",false);
        x.put("kidneyDisease",false);
        x.put("thyroidProblems",false);
        x.put("jointReplacement",false);
        x.put("lungDisease",false);
        x.put("stroke",false);
        x.put("asthmas",false);
        x.put("heartProblem",false);

        return x;

    }
}
