package com.example.registeryourself;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class resetPassword extends AppCompatActivity {

    Button button_ResetPassword;
    EditText editText_EmailVerify;

    FirebaseAuth dbAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        dbAuth = FirebaseAuth.getInstance();
        editText_EmailVerify = (EditText)findViewById(R.id.editText_EmailVerify);
        button_ResetPassword = (Button)findViewById(R.id.button_ResetPassword);
        button_ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rEmail = editText_EmailVerify.getText().toString().trim();
                if(rEmail.isEmpty()){
                    editText_EmailVerify.setError("enter a email id!");
                    return;
                }
               if(Patterns.EMAIL_ADDRESS.matcher(rEmail).matches()){
                   dbAuth.sendPasswordResetEmail(rEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),
                                        "check your email for reset password mail!",
                                        Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(),
                                        "Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                       }
                   });
               }else{
                   editText_EmailVerify.setError("Invalid Email!");
               }
            }
        });
    }
}