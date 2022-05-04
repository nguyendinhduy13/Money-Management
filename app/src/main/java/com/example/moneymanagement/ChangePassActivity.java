package com.example.moneymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

    private EditText edt_new_password, edt_old_password, edt_confirm_new_password;
    private Button btn_change_password;
    private ProgressDialog progressDialog;
    private TextView tv_show;
    private Boolean test = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        tv_show = findViewById(R.id.tv_show);

        edt_new_password = findViewById(R.id.edt_new_password);
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_confirm_new_password = findViewById(R.id.edt_confirm_new_password);
        btn_change_password = findViewById(R.id.btn_change_password);
        progressDialog = new ProgressDialog(this);

        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (test) {
                    edt_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_confirm_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edt_old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


                    tv_show.setText("Show");
                    test = false;
                } else {
                    edt_new_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    edt_confirm_new_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    edt_old_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    tv_show.setText("Hide");
                    test = true;
                }
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangePassword();
            }
        });
    }

    private void onClickChangePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String strNewPassword = edt_new_password.getText().toString().trim();
        String strNewConfirm = edt_confirm_new_password.getText().toString().trim();
        String pass = edt_old_password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            edt_old_password.setError("Current password is required");
        } else {
            if (TextUtils.isEmpty(strNewPassword)) {
                edt_new_password.setError("New password is required");
            } else {
                if (TextUtils.isEmpty(strNewConfirm)) {
                    edt_confirm_new_password.setError("Please re-enter your password");
                } else {
                    if (!strNewPassword.equals(strNewConfirm)) {
                        Toast.makeText(this, "Password confirmation failed", Toast.LENGTH_SHORT).show();
                    } else {
                        if (strNewPassword.equals(pass)) {
                            Toast.makeText(this, "The new password is the same as the current password", Toast.LENGTH_SHORT).show();
                        } else {
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(user.getEmail(), pass);

                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });

                            progressDialog.show();
                            user.updatePassword(strNewPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ChangePassActivity.this, "User password updated", Toast.LENGTH_SHORT).show();
                                                new AlertDialog.Builder(ChangePassActivity.this)
                                                        .setTitle("Money Management App")
                                                        .setMessage("Do you want to exit?")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                FirebaseAuth.getInstance().signOut();
                                                                Intent intent=new Intent(ChangePassActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        })
                                                        .setNegativeButton("No",null)
                                                        .show();
                                            }
                                            else {
                                                Toast.makeText(ChangePassActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }
                    }


                }
            }
        }

    }
}