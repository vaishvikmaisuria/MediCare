package com.quickhealth.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.quickhealth.medicare.model.User;
import com.quickhealth.medicare.model.userResponse;
import com.quickhealth.medicare.webservice.RetrofitClientInstance;
import com.quickhealth.medicare.webservice.Users;


import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

//    private INodeJs myApi;
    private  final CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText username, password,email,firstName,lastName,confirm;
    Button submit;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        Retrofit retrofit = RetrofitClient.getInstance();
//        myApi = retrofit.create(INodeJs.class);
        username = (EditText) findViewById(R.id.regUsername);
        password = (EditText) findViewById(R.id.regPassword);
        email = (EditText) findViewById(R.id.regEmail);
        firstName = (EditText) findViewById(R.id.regFirst);
        lastName = (EditText) findViewById(R.id.regLast);
        confirm = (EditText) findViewById(R.id.regConfirm);
        submit = (Button) findViewById(R.id.regButton);
        spinner = (ProgressBar) findViewById(R.id.spin);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirm.getText().toString())){
                    RegisterUser(email.getText().toString(),username.getText().toString(),firstName.getText().toString(),
                            lastName.getText().toString(),password.getText().toString());
                }else{

                    Snackbar.make(v,"Password and Confirm Password do not match", Snackbar.LENGTH_LONG).show();
                }

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

    public void gotologin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void RegisterUser(String email,String username,String firstName,String lastName,String password){
        spinner.setVisibility(View.VISIBLE);
//      Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
//      The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        Users users = retrofit.create(Users.class);
//      Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        Call<User> call;
        call = users.registerUser((firstName + lastName), username, email, password);

//      This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/

                if(response.isSuccessful()) {
                    Snackbar.make(getRootView(), "Registration Complete", Snackbar.LENGTH_LONG).show();
                    gotologin(getRootView());
                    spinner.setVisibility(View.INVISIBLE);
                }else{
                    Snackbar.make(getRootView(), "error", Snackbar.LENGTH_LONG).show();
                    spinner.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/
                Snackbar.make(getRootView(), "Big Failure", Snackbar.LENGTH_LONG).show();
            }
        });

    }
}
