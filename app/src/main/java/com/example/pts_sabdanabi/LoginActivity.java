package com.example.pts_sabdanabi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pts_sabdanabi.databinding.ActivityLoginnBinding;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.prefs.Preferences;
import java.util.zip.Inflater;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginnBinding binding;

    private String username, password;
    private ProgressDialog progressDialog;
    Preferencse preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginnBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferences = new Preferencse(this);
        if(preferences. getSessionLogin() == true){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = binding.emailLogin.getText().toString();
                password = binding.passwordLogin.getText().toString();

                if(formIsEmpty()){
                    Toast.makeText(LoginActivity.this, getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
                }else{
                    //Login Process
                    loginProcess();


                }
            }
        });
    }

    private boolean formIsEmpty(){
        return username.isEmpty() || password.isEmpty();
    }

    private void loginProcess(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = service.login(username,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                String res;
                try {
                    res = response.body().string();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if(res != null){
                    try {
                        JSONObject jsonResponses = new JSONObject(res);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                toastMessage("Something went wrong...Please try later!");
            }
        });

    }

    private void goToHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String value){
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

}


