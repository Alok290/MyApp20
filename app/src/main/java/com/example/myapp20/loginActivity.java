package com.example.myapp20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapp20.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    private EditText InputPhoneNumberEmailId,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton  = (Button) findViewById(R.id.login_btn);
        InputPhoneNumberEmailId  = (EditText) findViewById(R.id.login_phone_number_input);
        InputPassword  = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }

            private void LoginUser() {
                String phone = InputPhoneNumberEmailId.getText().toString();
                String password = InputPassword.getText().toString();

            }
        });
    }
    private void LoginUser() {
        String phone = InputPhoneNumberEmailId.getText().toString();
        String password = InputPassword.getText().toString();

     if (TextUtils.isEmpty(phone)) {
        Toast.makeText(this, "Please write your phone number & email id....", Toast.LENGTH_SHORT).show();
    }
    else if (TextUtils.isEmpty(password)) {
        Toast.makeText(this, "Please write your password....", Toast.LENGTH_SHORT).show();

    }
    else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("please wait, while we are checking the credential.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone,password);

        }

}

    private void AllowAccessToAccount(String phone, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.child(parentDbName).child(phone).exists()){
                    Users userData = datasnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(password)){

                            Toast.makeText(loginActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(loginActivity.this,HomeActivity2.class);
                            startActivity(intent);
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(loginActivity.this, "password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }


                }
                else{
                    Toast.makeText(loginActivity.this, "Account with this " +phone+"number do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}