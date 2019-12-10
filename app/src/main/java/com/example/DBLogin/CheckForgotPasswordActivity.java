package com.example.DBLogin;

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
import android.widget.Spinner;
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

public class CheckForgotPasswordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText emailAddressForgotPassword;
    private String hintQuestionForgotPassword;
    private EditText hintAnswerForgotPassword;
    private Button btnForgotPassword;
    private Spinner spinner;
    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_forgotpass);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerCheckForgotPassword);
        actionBarDrawerToggle = new ActionBarDrawerToggle(CheckForgotPasswordActivity.this, drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        spinner = (Spinner)findViewById(R.id.hintQuestionCheckForgotPasword);
        String[] item = new String[]{"apa hewan ke sukaan anda?", "apa makanan favorite anda?", "apa mobil ke sukaan anda?"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckForgotPasswordActivity.this,
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
        emailAddressForgotPassword = findViewById(R.id.emailAddressCheckForgotPassword);
        hintQuestionForgotPassword = spinner.getSelectedItem().toString();
        hintAnswerForgotPassword = findViewById(R.id.hintAnswerCheckForgotPassword);
        btnForgotPassword = findViewById(R.id.btnCheckForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requesCheckForgotPassword();
            }
        });
    }

    private void requesCheckForgotPassword(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email_address", emailAddressForgotPassword.getText().toString());
        jsonObject.addProperty("hint_question", hintQuestionForgotPassword);
        jsonObject.addProperty("hint_answer", hintAnswerForgotPassword.getText().toString());

        mApiService.checkForgotPassword(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            try {
                                JSONObject jsonResult = new JSONObject(response.body().string());
                                Log.i("debug", "onResponse: BERHASIL");
                                loading.dismiss();
                                int code = Integer.parseInt(jsonResult.getString("code"));
                                if(code == 200){
                                    String messageCode = jsonResult.getString("code_message");
                                    Toast.makeText(mContext, messageCode, Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email_address", emailAddressForgotPassword.getText().toString());
                                    Intent intent = new Intent(CheckForgotPasswordActivity.this, ForgotPassword.class);
                                    intent.putExtras(bundle);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    String errorMessage = jsonResult.getString("code_message");
                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }else {
                            loading.dismiss();
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
