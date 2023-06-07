package com.example.pts_sabdanabi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.pts_sabdanabi.databinding.ActivitySignUpBinding;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String email, password, confirmPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email = binding.emailRegister.getText().toString();
                password = binding.passwordRegister.getText().toString();
                confirmPassword = binding.passwordcom.getText().toString();

                if (formIsEmpty()) {
                    toastMessage(getString(R.string.error_field_required));
                } else if(!isUsernameValid(email)) {
                    toastMessage(getString(R.string.error_valid_email));
                }else if(!isEmailValid(password)) {
                    toastMessage(getString(R.string.error_valid_password));
                }else if(!isMinimalPasswordValid(password) || !isMinimalPasswordValid(confirmPassword)){
                    toastMessage(getString(R.string.error_minimal_password));
                }else if(!isPasswordMatch()){
                    toastMessage(getString(R.string.error_password_dont_match));
                }else{

                    //Login Process
                    loginProcess();

                }
            }
        });

    }

    private void toastMessage(String value){
        Toast.makeText(SignUpActivity.this, value, Toast.LENGTH_SHORT).show();
    }

    private boolean formIsEmpty() {
        return email.isEmpty() ||password.isEmpty() || confirmPassword.isEmpty();
    }

    private boolean isUsernameValid(String value){
        Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
        return pattern.matcher(value)
                .matches();
    }

    private boolean isEmailValid(String value) {
        return Patterns.EMAIL_ADDRESS.matcher(value)
                .matches();
    }

    private boolean isMinimalPasswordValid(String value){
        return value.length() >= 8;
    }

    private boolean isPasswordMatch(){
        return password.equals(confirmPassword);
    }

    private void loginProcess(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = service.registerUser(email,password);

        call.enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                toastMessage("Something went wrong...Please try later!");
            }

        });
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}