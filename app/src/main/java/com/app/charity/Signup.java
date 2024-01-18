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
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText firstName;
    EditText surname;
    EditText email;
    EditText phone;
    EditText password;
    Button  btnSignup;
    TextView Login;
    TextView Error;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName= findViewById(R.id.etSignupFirstName);
        surname=findViewById(R.id.etSignupSurname);
        email = findViewById(R.id.etSignupEmail);
        phone= findViewById(R.id.etSignupPhone);
        password=findViewById(R.id.etSignupPassword);
        btnSignup = findViewById(R.id.btnSignup);
        Login = findViewById(R.id.tvSignupLogin);
        Error=findViewById(R.id.tvLoginError);

        toast = Toast.makeText(this, "HELLO", Toast.LENGTH_LONG);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class );
                intent.putExtra("prevActivity","Signup");
                startActivity(intent);
                finish();
            }
        });


    }


    void validate(){

        if (firstName.getText().toString().isEmpty()){
            showError("Please enter your first name");
            return;
        }

        if (surname.getText().toString().isEmpty()){
            showError("Please enter your surname");
            return;
        }

        if (!isEmailValid(email.getText().toString())){
            showError("Please enter a valid email address");
            return;
        }
        if (!PhoneNumberUtils.isGlobalPhoneNumber("+27" + phone.getText().toString().substring(1, (phone.getText().toString().length()-1 )))){
            showError("Please enter a valid phone number");
            return;
        }

        if (password.getText().toString().length()==0) {
            showError("PLease enter a password");
            return;
        }

        if (!isPasswordValid(password.getText().toString())){
            showError("Password length must at least 6");
            return;
        }

        DatabaseHelper.addUsertoDB(getApplicationContext(),firstName.getText().toString(), surname.getText().toString(), email.getText().toString(),
                phone.getText().toString(), password.getText().toString(),
                new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {

                if (response.equals("error")) {
                    toast.setText("Email Already in Use");
                    toast.show();
                }
                else{
                    Intent intent = new Intent(Signup.this, MainActivity.class);
                    intent.putExtra("prevActivity", "Signup");
                    addUsertoSharedPreferences(Integer.parseInt(response));
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
        Error.setText(message);
        if (Error.getVisibility()== View.INVISIBLE)
            Error.setVisibility(View.VISIBLE);
    }
    void addUsertoSharedPreferences(Integer id){
        SharedPreferences sp=getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", Integer.toString(id));
        editor.commit();
    }




}
