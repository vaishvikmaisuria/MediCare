package com.quickhealth.medicare;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;
import com.quickhealth.medicare.model.userResponse;
import com.quickhealth.medicare.webservice.RetrofitClientInstance;
import com.quickhealth.medicare.webservice.Users;


import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    // public static JSONObject User = null;

    private  final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Button loginbtn, registerbtn;
    private EditText usernameET, passwordET;
    private TextView forgotPassword;
    private ProgressBar spinner;
    private RequestQueue mQueue;
    private String url = "http://216.13.26.101/users/login";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameET = (EditText) findViewById(R.id.useremailView);
        passwordET = (EditText) findViewById(R.id.passwordView);
        loginbtn = (Button) findViewById(R.id.loginButton);
        registerbtn = (Button) findViewById(R.id.registerButton);
        forgotPassword = (TextView) findViewById(R.id.forgotPass);

        spinner = (ProgressBar) findViewById(R.id.spin);
        mQueue = VolleyMySingleton.getInstance(this).getRequestQueue();
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser(usernameET.getText().toString(), passwordET.getText().toString());
                //Snackbar.make(getRootView(),usernameET.getText().toString()+ " " +passwordET.getText() , Snackbar.LENGTH_LONG).show();\
            }


        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoforgotpass(v);
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goregister(v);
            }
        });



    }

    private View getRootView() {
        final ViewGroup contentViewGroup = (ViewGroup) findViewById(android.R.id.content);
        View rootView = null;

        if(contentViewGroup != null)
            rootView = contentViewGroup.getChildAt(0);

        if(rootView == null)
            rootView = getWindow().getDecorView().getRootView();

        return rootView;
    }


    public void goregister(View view) {

        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void gotoforgotpass(View view) {
        Intent intent = new Intent(this, forgot_password.class);
        startActivity(intent);
    }

    public void gotomain(View view) {
        Intent intent = new Intent(this, menu.class);
        startActivity(intent);
    }



    private void LoginUser(String emailAddress, String password) {
        spinner.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.INVISIBLE);

//      Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        /*
        The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        */
        Users users = retrofit.create(Users.class);
        /*
        Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        */
        Call<userResponse> call;

        call = users.loginUser(emailAddress, password);
        /*
        This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        */
        call.enqueue(new Callback<userResponse>() {
            @Override
            public void onResponse(Call<userResponse> call, retrofit2.Response<userResponse> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/

                if(response.isSuccessful()) {

                    userResponse userID = response.body();


                    CurrentUser.setUserID(userID.getId());
                    CurrentUser.setUserToken(userID.getToken());
                    CurrentUser.setUserName(userID.getUserName());
                    gotomain(getRootView());
                    spinner.setVisibility(View.INVISIBLE);

                }else{
                    Snackbar.make(getRootView(), "response is not successfull", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<userResponse> call, Throwable t) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    Toast.makeText(MainActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }
            }
        });
    }
}
