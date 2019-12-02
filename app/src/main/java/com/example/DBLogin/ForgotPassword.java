package com.example.DBLogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPassword extends AppCompatActivity {
    private EditText newPassword;
    private EditText confirmPassword;
    private Button btnForgotPassword;
    private Context mContext;
    private BaseApiService mApiService;
    private ProgressDialog loading;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        iniComponents();
    }

    public void iniComponents(){
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestForgotPassword();
            }
        });
    }

    public void requestForgotPassword(){
        Bundle bundle = getIntent().getExtras();
        String emailAddress = bundle.getString("email_address");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email_address", emailAddress);
        jsonObject.addProperty("new_password", newPassword.getText().toString());

        mApiService.forgotPassword(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if(response.isSuccessful()){
                                Log.i("debug", "onResponse: BERHASIL");
                                loading.dismiss();
                                int code = Integer.parseInt(jsonObject.getString("code"));
                                if(code == 200){
                                    String messageCode = jsonObject.getString("code_message");
                                    Toast.makeText(mContext, messageCode, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, com.example.DBLogin.LoginActivity.class));
                                }else {
                                    String errorMessage = jsonObject.getString("code_message");
                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Log.i("debug", "onResponse: GA BERHASIL");
                                String errorMessage = jsonObject.getString("code_message");
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
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
