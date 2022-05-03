package com.example.moneymanagement;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.BitSet;

public class ProfileActivity extends AppCompatActivity {

    private TextView tv_name,logoutBtn,tv_account,tv_changepass;
    private ImageView img_avatar;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutBtn=findViewById(R.id.logoutBtn);
        tv_name=findViewById(R.id.tv_name);
        img_avatar=findViewById(R.id.img_avatar);
        tv_account=findViewById(R.id.tv_account);
        tv_changepass=findViewById(R.id.tv_changepass);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        bottomNavigationView.setBackground(null);




        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_history:
                        startActivity(new Intent(ProfileActivity.this,HistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.action_home:
                        startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.action_profile:
                        return true;
                    case R.id.action_chart:
                        startActivity(new Intent(ProfileActivity.this,ChooseAnalyticActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });
        tv_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ChangePassActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Money Management App")
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent=new Intent(ProfileActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        showUserInformation();
    }



    private void showUserInformation(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String name = user.getDisplayName();
        Uri photoUrl = user.getPhotoUrl();

        tv_name.setText(name);
        //tv_name.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Glide.with(this).load(photoUrl).error(R.drawable.ic_person_24).into(img_avatar);
    }
}