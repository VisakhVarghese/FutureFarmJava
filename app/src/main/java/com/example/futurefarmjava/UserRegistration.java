package com.example.futurefarmjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.futurefarmjava.Firebase.UserHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SnapshotHolder;

public class UserRegistration extends AppCompatActivity {

    private EditText name, phoneNumber, email, password, rePassword;
    private AppCompatButton backBtn, regBtn;
    int ph_check,email_check;
    FirebaseDatabase rootNode;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        name = findViewById(R.id.reg_name);
        phoneNumber = findViewById(R.id.reg_phone);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        rePassword = findViewById(R.id.reg_rePassword);
        backBtn = findViewById(R.id.back_btn);
        regBtn = findViewById(R.id.register_button2);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegistration.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                saveUser();
            }


        });

    }

    private void saveUser() {

        int passwordCheck = 0;




            //get all values
            String t_name = name.getText().toString().trim();
            String t_phone = phoneNumber.getText().toString().trim();
            String t_email = email.getText().toString().trim();
            String t_password = password.getText().toString().trim();



        if(TextUtils.isEmpty(name.getText().toString())){
            name.setError("Enter User name or phone number");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber.getText().toString().trim())){
            phoneNumber.setError("Enter phone number");
            return;
        }
        if(TextUtils.isEmpty(email.getText().toString().trim())){
            phoneNumber.setError("Enter phone number");
            return;
        }
        if(TextUtils.isEmpty(password.getText().toString().trim())){
            password.setError("Enter phone number");
            return;
        }
        if(TextUtils.isEmpty(rePassword.getText().toString().trim())){
            rePassword.setError("Enter phone number");
            return;
        }
        /*
        if(password.length()<8){
            password.setError("Password should carry atleast 8 characters");
            return;
        }else if(password != rePassword){
            rePassword.setError("Password not match");
            return;
        }
        else{
            passwordCheck = 1;
        }

         */


        passwordCheck=1;


       //     String id = reference.push().getKey();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");


        UserHelperClass helperClass = new UserHelperClass(t_name, t_phone, t_email, t_password);


        reference.orderByChild("phoneNumber").equalTo(t_phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            Toast.makeText(getApplicationContext(), "Phone number already used", Toast.LENGTH_SHORT).show();
                        }else{
                            reference.child(t_phone).setValue(helperClass);
                            Toast.makeText(getApplicationContext(), "User is added", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        checkPhone();

        ph_check = 1;
        email_check = 1;


    }

    private void checkPhone() {


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(UserRegistration.this, Login.class);
        startActivity(intent);
        finish();


    }
}

