package com.example.futurefarmjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.futurefarmjava.Firebase.UserHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    Button login_btn,register_btn;
    EditText user_name,user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.btn_login);
        register_btn = findViewById(R.id.btn_register);
        user_name = findViewById(R.id.editText);
        user_password = findViewById(R.id.editText2);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(user_name.getText().toString())){
                    user_name.setError("Enter User name or phone number");
                }
                else if(TextUtils.isEmpty(user_password.getText().toString())){
                    user_password.setError("Enter password");
                }else{
                    user_name.setError(null);
                    user_password.setError(null);
                    isUser();
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, UserRegistration.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void isUser() {
        String userEnteredUserName = user_name.getText().toString().trim();
        String userEnteredPassword = user_password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredUserName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    user_name.setError(null);
                    reference.child(userEnteredUserName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            UserHelperClass user = snapshot.getValue(UserHelperClass.class);

                            assert user != null;
                            if(user.getPassword().equals(userEnteredPassword)){

                                Log.d(TAG,user.getPassword());
                                user_password.setError(null);
                                String nameFromDatabase = user.getPhoneNumber();
                                String phoneFromDatabase = user.getPassword();
                                String emailFromDatabase = user.getEmail();

                                Intent intent = new Intent(getApplicationContext(),Home.class);
                                intent.putExtra("email",emailFromDatabase);
                                intent.putExtra("name",nameFromDatabase);
                                intent.putExtra("phoneNumber",phoneFromDatabase);

                                startActivity(intent);
                            }
                            else{
                                user_password.setError("Wrong password");
                                user_password.requestFocus();
                                Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    user_name.setError("No user found");
                    user_name.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}