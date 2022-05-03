package com.example.moneymanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class AccountActivity extends AppCompatActivity {

    private ImageView img_avatar;
    private TextView tv_name;
    private Button btn_capnhap,btn_capnhapemail;
    private EditText edtFullName,edtEmail,edtPass;
    private Uri muri;
    private ProgressDialog progressDialog;

    final private ActivityResultLauncher<Intent> mActivityResultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK)
                    {
                        Intent intent=result.getData();
                        if(intent==null){
                            return;
                        }
                        Uri uri=intent.getData();
                        setMuri(uri);
                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri) ;
                            img_avatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        img_avatar=findViewById((R.id.img_avatar));
        tv_name=findViewById((R.id.tv_name));
        btn_capnhap=findViewById((R.id.btn_capnhap));
        btn_capnhapemail=findViewById((R.id.btn_capnhapemail));
        edtFullName=findViewById((R.id.edtFullName));
        edtEmail=findViewById((R.id.edtEmail));
        edtPass=findViewById((R.id.edtPass));

        progressDialog=new ProgressDialog(this);


        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String email=user.getEmail();
        String name = user.getDisplayName();
        edtEmail.setText(email);
        edtFullName.setText(name);
        Uri photoUrl = user.getPhotoUrl();
        Glide.with(this).load(photoUrl).error(R.drawable.ic_person_24).into(img_avatar);
        muri=photoUrl;


        initListener();
        showUserInformation();
    }

    private void initListener() {
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        btn_capnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
        btn_capnhapemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateEmail();
            }
        });
    }

    private void onClickUpdateEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        String pass=edtPass.getText().toString();
        if(TextUtils.isEmpty(pass)){
            edtPass.setError("Password is required");
        }
        else {

            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, pass);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });

            String strNewEmail = edtEmail.getText().toString().trim();
            progressDialog.show();

            user.updateEmail(strNewEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(AccountActivity.this, "Update email success", Toast.LENGTH_SHORT).show();
                                showUserInformation();
                            }
                        }
                    });
        }
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.show();
        if(user==null){
            return;
        }
        String strFullName=edtFullName.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(muri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountActivity.this, "Update profile success", Toast.LENGTH_SHORT).show();
                            showUserInformation();
                        }
                        Intent intent=new Intent(AccountActivity.this,ProfileActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private void onClickRequestPermission() {
        openGallery();
    }

    public void setMuri(Uri muri) {
        this.muri = muri;
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }

    private void showUserInformation(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String name = user.getDisplayName();
        Uri photoUrl = user.getPhotoUrl();

        tv_name.setText(name);
        Glide.with(this).load(photoUrl).error(R.drawable.ic_person_24).into(img_avatar);
    }
}