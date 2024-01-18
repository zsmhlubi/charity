package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    EditText etLoginEmail;
    EditText etLoginPhone;
    EditText etLoginPassword;
    Button BtnLogin;
    TextView tvLoginSignup;
    TextView tvLoginForgotPassword;
    TextView tvLoginError;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toast = Toast.makeText(this, "HELLO", Toast.LENGTH_LONG);

        //page setup
        etLoginEmail = findViewById(R.id.login_email);
        etLoginPhone =findViewById(R.id.login_phone);
        etLoginPassword = findViewById(R.id.login_password);
        BtnLogin = findViewById(R.id.login_button);
        tvLoginForgotPassword= findViewById(R.id.login_forgotpassword);
        tvLoginSignup=findViewById(R.id.login_signup);
        tvLoginError= findViewById(R.id.tvLoginError);


        //set on clicklistener for login button
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        tvLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup_intent = new Intent(Login.this, Signup.class );
                startActivity(signup_intent);
                finish();
            }
        });
    }

    void validate(){
        if (!isEmailValid(etLoginEmail.getText().toString())){
            showError("Please enter a valid email address");
            return;
        }
        if (!PhoneNumberUtils.isGlobalPhoneNumber("+27" + etLoginPhone.getText().toString().substring(1, (etLoginPhone.getText().toString().length()-1 )))){
            showError("Please enter a valid phone number");
            return;
        }

        if (etLoginPassword.getText().toString().length()==0) {
            showError("PLease enter a password");
            return;
        }

        if (!isPasswordValid(etLoginPassword.getText().toString())){
            showError("Password length must at least 6");
            return;
        }

        DatabaseHelper.getUserId(this, etLoginEmail.getText().toString(), etLoginPhone.getText().toString(), etLoginPassword.getText().toString(),
                new DatabaseHelper.Callback() {
                    @Override
                    public void myResponseCallback(String response) throws JSONException {

                        System.out.println(response);

                        if (response.equals("Error")) {
                            toast.setText("Invalid Login Details. Please Try Again");
                            toast.show();
                        }

                        else{
                            addUsertoSharedPreferences(Integer.parseInt(response));
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("prevActivity", "Login");
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }



    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    boolean isPasswordValid(String password){
        if (password.length()<6)
            return false;
        return true;
    }
    void showError(String message){
        tvLoginError.setText(message);
        if (tvLoginError.getVisibility()== View.INVISIBLE)
            tvLoginError.setVisibility(View.VISIBLE);
    }


    void addUsertoSharedPreferences(Integer id){
        SharedPreferences sp=getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", Integer.toString(id));
        editor.commit();
    }


}
