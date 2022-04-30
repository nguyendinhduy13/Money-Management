package com.example.moneymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChooseAnalyticActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytic);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.action_chart);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_history:
                        startActivity(new Intent(ChooseAnalyticActivity.this,HistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.action_home:
                        startActivity(new Intent(ChooseAnalyticActivity.this,HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.action_profile:
                        startActivity(new Intent(ChooseAnalyticActivity.this,ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.action_chart:
                        return true;

                }
                return false;
            }
        });
    }
}