package com.example.DBLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText userNameRegister;
    private EditText emailAddressRegister;
    private EditText passwordRegister, phoneNumberRegister, hintAnswer;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnRegister;
    private ProgressDialog loading;
    private String hintQuestion,gender = "";
    private Context mContext;
    private BaseApiService mApiService;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerRegister);
        actionBarDrawerToggle = new ActionBarDrawerToggle(RegisterActivity.this, drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        spinner = (Spinner)findViewById(R.id.hintQuestion);
        String[] item = new String[]{"apa hewan ke sukaan anda?", "apa makanan favorite anda?", "apa mobil ke sukaan anda?" +
                ""};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this,
                android.R.layout.simple_spinner_item,item);
        spinner.setAdapter(adapter);

        initComponents();
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
        return false;
    }

    public void initComponents(){
        userNameRegister = (EditText) findViewById(R.id.userNameRegister);
        passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        emailAddressRegister = (EditText) findViewById(R.id.emailAddress);
        phoneNumberRegister = (EditText) findViewById(R.id.phoneNumber);
        radioGroup = findViewById(R.id.radiogroup);
        hintQuestion = spinner.getSelectedItem().toString();
        hintAnswer = (EditText) findViewById(R.id.hintAnswer);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if(selectedRadioButtonId != -1){
                    radioButton = findViewById(selectedRadioButtonId);
                    gender = radioButton.getText().toString();
                }else {
                    Toast.makeText(mContext, "Please Selected Gender", Toast.LENGTH_SHORT).show();
                }

                requestRegister();
            }
        });
    }

    private void requestRegister(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", userNameRegister.getText().toString());
        jsonObject.addProperty("password", passwordRegister.getText().toString());
        jsonObject.addProperty("email_address", emailAddressRegister.getText().toString());
        jsonObject.addProperty("phone_number", phoneNumberRegister.getText().toString());
        jsonObject.addProperty("gender", gender);
        jsonObject.addProperty("hint_question", hintQuestion);
        jsonObject.addProperty("hint_answer", hintAnswer.getText().toString());

        mApiService.registerRequest(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.body() != null){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.i("debug", "onResponse: BERHASIL");
                                loading.dismiss();
                                int code = Integer.parseInt(jsonRESULTS.getString("code"));
                                System.out.println("response_data" + code);
                                if (code == 200) {
                                    String messageCode = jsonRESULTS.getString("code_message");
                                    Toast.makeText(mContext, messageCode, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, com.example.DBLogin.LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                } else {
                                    String errorMessage = jsonRESULTS.getString("code_message");
                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            loading.dismiss();
                            Toast.makeText(mContext, "Some Field Is Empty", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


