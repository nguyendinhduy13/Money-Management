package com.example.moneymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout linearNgansach;
    private TextView todaytv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linearNgansach=findViewById(R.id.linearNgansach);
        linearNgansach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(HomeActivity.this,BudgetActivity.class);
                startActivity(intent);
            }
        });

        todaytv=findViewById(R.id.todaytv);
        todaytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(HomeActivity.this,TodaySpendingActivity.class);
                startActivity(intent);
            }
        });
    }
}