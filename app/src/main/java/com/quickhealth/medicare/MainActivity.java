package com.quickhealth.medicare;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.quickhealth.medicare.Retrofit.INodeJs;
import com.quickhealth.medicare.Retrofit.RetrofitClient;
import com.quickhealth.medicare.User;
import org.json.JSONObject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    // public static JSONObject User = null;
    private INodeJs myApi;
    private  final CompositeDisposable compositeDisposable = new CompositeDisposable();


    private Button loginbtn, registerbtn;
    private EditText usernameET, passwordET;
    private TextView forgotPassword;
    private ProgressBar spinner;

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

        usernameET = (EditText) findViewById(R.id.usernameView);
        passwordET = (EditText) findViewById(R.id.passwordView);
        loginbtn = (Button) findViewById(R.id.loginButton);
        registerbtn = (Button) findViewById(R.id.registerButton);
        forgotPassword = (TextView) findViewById(R.id.forgotPass);
        Retrofit retrofit = RetrofitClient.getInstance();
        myApi = retrofit.create(INodeJs.class);
        spinner = (ProgressBar) findViewById(R.id.spin);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser(usernameET.getText().toString(),passwordET.getText().toString());
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
        Intent intent = new Intent(this, homepage.class);
        startActivity(intent);
    }



    private void LoginUser(String username,String password){
        spinner.setVisibility(View.VISIBLE);
        compositeDisposable.add(myApi.loginUser(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {

                                   JSONObject response = new JSONObject(s);
                                   if(response.has("error")){
                                       //Log.d(LOG_TAG, "Register button clicked!");
                                       Snackbar.make(getRootView(), response.get("error").toString(), Snackbar.LENGTH_LONG).show();
                                       spinner.setVisibility(View.INVISIBLE);
                                   }else {
                                       User.setUser(response);
                                       gotomain(getRootView());
                                       spinner.setVisibility(View.INVISIBLE);
                                   }
                               }
                           }
                ));


    }

}
