package com.example.DBLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DBLogin.api.BaseApiService;
import com.example.DBLogin.api.UtilsApi;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText userNameLogin;
    EditText passwordLogin;
    Button btnLogin;
    Button btnRegister;
    ProgressDialog loading;
    TextView forgotPassword;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper
        initComponents();
    }

    public void initComponents() {
        userNameLogin = (EditText) findViewById(R.id.userNameLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestLogin();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, com.example.DBLogin.RegisterActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Redirecting...",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, CheckForgotPasswordActivity.class));
            }
        });

    }

    private void requestLogin() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", userNameLogin.getText().toString());
        jsonObject.addProperty("password", passwordLogin.getText().toString());
        mApiService.loginRequest(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                int code = Integer.parseInt(jsonRESULTS.getString("code"));
                                if (code == 200) {
                                    String messageCode = jsonRESULTS.getString("code_message");
                                    Toast.makeText(mContext, messageCode, Toast.LENGTH_SHORT).show();
                                    String nama = "Ricky";
                                    Intent intent = new Intent(mContext, com.example.DBLogin.MainActivity.class);
                                    intent.putExtra("result_nama", nama);
                                    startActivity(intent);
                                } else {
                                    // Jika login gagal
                                    String errorMessage = jsonRESULTS.getString("code_message");
                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }
}
