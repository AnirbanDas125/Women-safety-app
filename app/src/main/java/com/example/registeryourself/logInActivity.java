package com.example.registeryourself;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class logInActivity extends AppCompatActivity {

    Button button_Register,button_LogIn,button_ForgetPassword;
    EditText editText_Email,editText_Password;
    FirebaseAuth dbAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editText_Email=findViewById(R.id.editText_Email);
        editText_Password=findViewById(R.id.editText_Password);
        dbAuth = FirebaseAuth.getInstance();
        button_ForgetPassword = findViewById(R.id.button_ForgetPassword);
        button_LogIn=findViewById(R.id.button_LogIn);
        button_LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = editText_Email.getText().toString();
                String pass = editText_Password.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    editText_Email.setError("Email is Requires!");
                    return;
                }
                if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()==false){
                    editText_Email.setError("Invalid email!");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    editText_Password.setError("Password Required");
                    return;
                }
                if(pass.length() < 6){
                    editText_Password.setError("Password Cannot be < 6 Characters!");
                    return;
                }

                dbAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Logged In Successfully!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MyLocation.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), "Wrong Email Id/Password !",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        button_Register=findViewById(R.id.button_Register);
        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        button_ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),resetPassword.class);
                startActivity(intent);
            }
        });

    }
}