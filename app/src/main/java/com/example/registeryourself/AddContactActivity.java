package com.example.registeryourself;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {

    ImageButton imageButton_Contacts;
    EditText editText_ContactNo,editText_ContactName;
   // TextView contactCounter;
    Button button_Save;
    Intent intent ;
   //public String contactNoList[] = new String [5];
   //public int counter = 0;
   public static final String Name_tag1 = "ContactNo";
    public static final String Name_tag2 = "ContactName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editText_ContactNo = (EditText)findViewById(R.id.editText_ContactNo);
        editText_ContactName = (EditText)findViewById(R.id.editText_ContactName);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                      permissionToken.continuePermissionRequest();
                    }
                }).check();

        button_Save = (Button)findViewById(R.id.button_Save);
        imageButton_Contacts = (ImageButton)findViewById(R.id.imageButton_Contacts);
        imageButton_Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent,101);
            }
        });
        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNo = editText_ContactNo.getText().toString().trim();
                String contactName = editText_ContactName.getText().toString().trim();

                    intent = new Intent(getApplicationContext(),MyLocation.class);
                    intent.putExtra(Name_tag1,contactNo);
                    intent.putExtra(Name_tag2,contactName);
                    startActivity(intent);
                Toast.makeText(AddContactActivity.this, "Contact Saved!", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == Activity.RESULT_OK){
            Cursor cursor1 ;
            Uri contactUri = data.getData();
             cursor1 = getContentResolver().query(contactUri,null,null
                    ,null,null);
             if(cursor1.moveToFirst()){
                 String contactNo = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                 editText_ContactNo.setText(contactNo);
                 String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                 editText_ContactName.setText(contactName);
             }
        }
    }
}