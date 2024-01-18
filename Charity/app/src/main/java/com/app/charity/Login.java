package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        //check if email/phone combination is in database -> return true or invalid combination
        //check if password in database matches input password
        //From database get user ID
        //Map<String, String> mymap = new HashMap<String,String>();
        //mymap.put("name","John");
        //mymap.put("surname", "Greene");
        //DatabaseHelper dbHelper = new DatabaseHelper();
        //dbHelper.Save(this, mymap, "have");

        DatabaseHelper.getUserID(this, etLoginEmail.getText().toString(), etLoginPhone.getText().toString(), etLoginPassword.getText().toString());
        addUsertoSharedPreferences(DatabaseHelper.userId);
        Intent intent = new Intent(Login.this, MainActivity.class).putExtra("previousActivity", "Login");
        startActivity(intent);
        finish();
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
