package com.example.registeryourself;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    EditText editText_FirstName,editText_LastName,editText_PhoneNo,editText_Password,editText_Email;
    Button button_Register,button_LogIn;
    Intent intent;
    FirebaseAuth dbAuth = FirebaseAuth.getInstance();;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbRoot = db.getReference().child("Users");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        editText_FirstName=(EditText)findViewById(R.id.editText_FirstName);
        editText_LastName=(EditText)findViewById(R.id.editText_LastName);
        editText_PhoneNo=(EditText)findViewById(R.id.editText_PhoneNo);
        editText_Password=(EditText)findViewById(R.id.editText_Password);
        editText_Email=(EditText)findViewById(R.id.editText_Email);
        button_Register=(Button)findViewById(R.id.button_Register);
        button_LogIn=(Button)findViewById(R.id.button_LogIn);
        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = editText_FirstName.getText().toString().trim();
                String lName = editText_LastName.getText().toString().trim();
                String cID = editText_PhoneNo.getText().toString().trim();
               String Email = editText_Email.getText().toString().trim();
               String pass=editText_Password.getText().toString().trim();

                if(TextUtils.isEmpty(Email)){
                    editText_Email.setError("Email is Requires!");
                    return;
                }
                if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()==false){
                    editText_Email.setError("Invalid Email!");
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
                   dbAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(Task<AuthResult> task) {
                           if(task.isSuccessful() == true){

                               LinkedHashMap<String,String> userMap = new LinkedHashMap<>();
                               userMap.put("fName",fName);
                               userMap.put("lName",lName);
                               userMap.put("Phn",cID);
                               userMap.put("Email",Email);
                               userMap.put("pass",pass);
                               dbRoot.child(fName+" "+lName).setValue(userMap);

                               Toast.makeText(getApplicationContext(),"Account Created!",
                                       Toast.LENGTH_LONG).show();
                               intent = new Intent(getApplicationContext(),MyLocation.class);
                               startActivity(intent);
                           }else if(task.isCanceled()) {
                               Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           editText_Email.setError(e.getMessage());
                           return;
                       }
                   });

            }
        });
        button_LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),logInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(dbAuth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(),MyLocation.class);
            startActivity(intent);
            finish();
        }
    }
}