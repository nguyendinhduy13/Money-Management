package com.example.moneymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

//    private EditText edt_new_password,edt_old_password;
//    private Button btn_change_password;
//    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

//        edt_new_password=findViewById(R.id.edt_new_password);
//        edt_old_password=findViewById(R.id.edt_old_password);
//        btn_change_password=findViewById(R.id.btn_change_password);
//        progressDialog=new ProgressDialog(this);
//
//        btn_change_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickChangePassword();
//            }
//        });
//
//    }
//
//    private void onClickChangePassword() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String strNewPassword=edt_new_password.getText().toString().trim();
//        String pass=edt_old_password.getText().toString();
//        if(TextUtils.isEmpty(strNewPassword)){
//            edt_new_password.setError("Password is required");
//        }
//        else {
//
//            AuthCredential credential = EmailAuthProvider
//                    .getCredential(user.getEmail(), pass);
//
//            user.reauthenticate(credential)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                        }
//                    });
//
//        progressDialog.show();
//        user.updatePassword(strNewPassword)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ChangePassActivity.this, "User password updated", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
}