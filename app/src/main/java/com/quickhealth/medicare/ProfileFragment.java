package com.quickhealth.medicare;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.quickhealth.medicare.httpRequestHelpers.httpPostRequest;
import com.quickhealth.medicare.model.userResponse;
import com.quickhealth.medicare.webservice.RetrofitClientInstance;
import com.quickhealth.medicare.webservice.UserProfile;
import com.quickhealth.medicare.webservice.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView name;
    private TextView username;
    ImageView profileImage;
    private static final int PICK_FROM_GALLERY = 101;
    String endpoint = "https://quick-health.herokuapp.com";
    String realPath;
    private Bitmap photo;
    private Switch aSwitch;
    String picturePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_profile, container, false);

//        setContentView(R.layout.activity_profile);
        profileImage = v.findViewById(R.id.imageView);
        name = (TextView) v.findViewById(R.id.nameTextView);
        username = (TextView) v.findViewById(R.id.username);
        aSwitch = (Switch) v.findViewById(R.id.switch1);

        updateFields();

        Button button = (Button) v.findViewById(R.id.button8);
        Button emergencyInfo = (Button) v.findViewById(R.id.button7);
        Button personalInfo = (Button) v.findViewById(R.id.button6);

        Button logoutB = (Button) v.findViewById(R.id.logout);
        button.setOnClickListener(this);
        personalInfo.setOnClickListener(this);

        emergencyInfo.setOnClickListener(this);

        profileImage.setOnClickListener(this);



        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // makes sure Permissions are granted as of latest Android versions
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        return v;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImage = data.getData();

            // the path to the image in the phone
            realPath = ImageFilePath.getPath(this.getActivity(), selectedImage);

            // store photo path in database
            storePhotoPath(realPath);

            photo = BitmapFactory.decodeFile(realPath);

            // crops image, makes into a circle and sets as profile picture
            performCrop();

        } else {
            Toast.makeText(this.getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }
    // crops image, makes into a circle and sets as profile picture
    private void performCrop() {
        int h = 0;
        if(photo.getWidth()< photo.getHeight()){
            h = photo.getWidth();
        }else{
            h = photo.getHeight();
        }
        Bitmap croppedBmp = Bitmap.createBitmap(photo, 0, 0, h, h);
        profileImage.setImageBitmap(getCircleBitmap(croppedBmp));
    }

    // makes a image circular
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public  ProfileFragment() {}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button8: {
                Intent intent = new Intent(getActivity(), Medical_History.class);
                startActivity(intent);
                break;
            }
            case R.id.button6: {
                Intent intent = new Intent(getActivity(), personal_info.class);
                startActivity(intent);
                break;
            }
            case R.id.button7: {
                Intent intent = new Intent(getActivity(), emergency_contact.class);
                startActivity(intent);
            }
            case R.id.imageView: {
                openGallery();

            }
        }
    }

    /**
     *  Load user fields
     *
     */
    private void updateFields() {
        name.setText(CurrentUser.getUserName());
        username.setText(CurrentUser.getUserName());
    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        Log.d("Open Gallery", "In here");
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);
    }
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(ValidateDoctor.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            //Toast.makeText(ValidateDoctor.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    /*
     *   Store photo path
     */
    public void storePhotoPath(String path) {
        // Get current logged in user's id
        String id = CurrentUser.getUserID();
//      Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
//      The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        UserProfile usersProfile = retrofit.create(UserProfile.class);
//      Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        Call<String> call;
        String token = CurrentUser.getUserToken();
        Log.d("HolyFuck", "Hello" + CurrentUser.getUserToken() );
        call = usersProfile.storePath(id, path, token);

//      This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
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
    }




    /*
     *
     */
    public String getPhotoPath() {
        String photoPath = null;

        // Get current logged in user's id
        String id = CurrentUser.getUserID();

        // Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        // The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        UserProfile usersProfile = retrofit.create(UserProfile.class);
        // Invoke the method corresponding to the HTTP request which will return a Call object. This Call object will used to send the actual network request with the specified parameters
        Call<String> call;
        String token = CurrentUser.getUserToken();
        Log.d("HolyFuck", CurrentUser.getUserToken() );
        call = usersProfile.getPath(id, token);

        // This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/
                if(response.isSuccessful()) {
                    Log.d("myTag", response.body());
                    CurrentUser.setUserProfilePic(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of Json*/

            }
        });
        return CurrentUser.getUserProfilePic();

    }



    private void logout(){
        CurrentUser.setUserName(null);
        CurrentUser.setUserID(null);
        CurrentUser.setUserToken(null);
        CurrentUser.setUserProfilePic(null);
        CurrentUser.setUserLattitude(null);
        CurrentUser.setUserLongitude(null);

        Intent intentOn2 = new Intent(getActivity(), MainActivity.class);
        startActivity(intentOn2);


    }
}
