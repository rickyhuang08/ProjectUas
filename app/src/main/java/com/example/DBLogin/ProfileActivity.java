package com.example.DBLogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.DBLogin.api.BaseApiService;
import com.example.DBLogin.api.UtilsApi;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context mContext;
    private BaseApiService baseApiService;
    private String emailAddress;
    private String gender;
    private String phoneNumber;
    private String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = this;
        baseApiService = UtilsApi.getAPIService();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerProfile);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ProfileActivity.this, drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        profileUser();
    }

    private void profileUser(){
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        baseApiService.profileUser(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                JSONObject object = new JSONObject(jsonObject1.getString("data"));
                                emailAddress = object.getString("email_address");
                                gender = object.getString("gender");
                                phoneNumber = object.getString("phone_number");
                                userName = object.getString("username");
                                setTextUsername();
                                setTextEmail();
                                setTextPhoneNumber();
                                setTextGender();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });

    }

    public void setTextUsername(){
        final TextView textView;
        textView = findViewById(R.id.textUsername);
        textView.setText(userName);
    }
    public void setTextEmail(){
        final TextView textView;
        textView = findViewById(R.id.textViewEmail);
        textView.setText(emailAddress);
    }
    public void setTextPhoneNumber(){
        final TextView textView;
        textView = findViewById(R.id.textPhoneNumber);
        textView.setText(phoneNumber);
    }
    public void setTextGender(){
        final TextView textView;
        textView = findViewById(R.id.textGender);
        textView.setText(gender);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.home) {
            Toast.makeText(this, "This is Home", Toast.LENGTH_SHORT).show();
            Bundle bundle = getIntent().getExtras();
            String data = bundle.getString("data");

            bundle = new Bundle();
            bundle.putString("data", data);
            bundle.putString("username", userName);
            Intent intent = new Intent(this, showVideo.class);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if(id == R.id.account){
            Bundle bundle = getIntent().getExtras();
            String data = bundle.getString("data");
            bundle = new Bundle();
            bundle.putString("username", userName);
            bundle.putString("data", data);

            Intent intent = new Intent(ProfileActivity.this, com.example.DBLogin.ProfileActivity.class);
            intent.putExtras(bundle);
            Toast.makeText(this, "This is Account", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if(id == R.id.logout){
            Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, com.example.DBLogin.LoginActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        return false;
    }
}
