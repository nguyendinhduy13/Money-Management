package com.example.moneymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button registerQn,registerBtn;
    private EditText password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        registerQn=findViewById(R.id.registerQn);
        registerBtn=findViewById(R.id.registerBtn);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);

        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        registerQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString =email.getText().toString();
                String passwordString=password.getText().toString();

                if(TextUtils.isEmpty(emailString)){
                    email.setError("Email is required");
                }
                if(TextUtils.isEmpty(passwordString)){
                    password.setError("Password is required");
                }
                else
                {
                    progressDialog.setMessage("registration in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }
                            else {

                                Toast.makeText(RegistrationActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }
}