package com.quickhealth.medicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.quickhealth.medicare.model.User;
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

public class forgot_password extends AppCompatActivity {

    private EditText recovery_email;
    private Button reset_password;

    private  final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        recovery_email = (EditText) findViewById(R.id.txtResetEmail);
        reset_password = (Button) findViewById(R.id.btRecover);

        spinner = (ProgressBar) findViewById(R.id.spin);

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPass(recovery_email.getText().toString());
                //Snackbar.make(getRootView(),usernameET.getText().toString()+ " " +passwordET.getText() , Snackbar.LENGTH_LONG).show();
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




    private void forgotPass(String email) {
        spinner.setVisibility(View.VISIBLE);

        //      Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
//      The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        Users users = retrofit.create(Users.class);
//      Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        Call<String> call;
        call = users.forgetPassword(email);

//      This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/

                if(response.isSuccessful()) {
                    Snackbar.make(getRootView(), "Email has been sent", Snackbar.LENGTH_LONG).show();
                    gotologin(getRootView());
                    spinner.setVisibility(View.INVISIBLE);
                }else{
                    Snackbar.make(getRootView(), "error", Snackbar.LENGTH_LONG).show();
                    spinner.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/
                Snackbar.make(getRootView(), "Big Failure", Snackbar.LENGTH_LONG).show();
            }
        });
//        compositeDisposable.add(myApi.forgotPassword(email)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                               @Override
//                               public void accept(String s) throws Exception {
//                                   JSONObject response = new JSONObject(s);
//                                   if(response.has("error")){
//                                       Snackbar.make(getRootView(), response.get("error").toString(), Snackbar.LENGTH_LONG).show();
//                                       spinner.setVisibility(View.INVISIBLE);
//                                   }else {
//                                       User.setUser(response);
//                                       gotologin(getRootView());
//                                       spinner.setVisibility(View.INVISIBLE);
//
//                                   }
//                               }
//                           }
//                )
//        );
    }

    public void gotologin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
