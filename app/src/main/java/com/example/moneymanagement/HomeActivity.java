package com.example.moneymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout linearNgansach;
    private TextView tv_homnay,tvthunhap;
    private DatabaseReference budgetRef,personalRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linearNgansach=findViewById(R.id.linearNgansach);
        tvthunhap=findViewById(R.id.tv_thunhap);
        mAuth = FirebaseAuth.getInstance();
        budgetRef= FirebaseDatabase.getInstance().getReference().child("budget").child(mAuth.getCurrentUser().getUid());

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    int totalamount=0;
                    for(DataSnapshot snap:snapshot.getChildren()) {
                        Data data = snap.getValue(Data.class);

                        totalamount += data.getAmount();

                        String sttotal = String.valueOf(totalamount+"$");

                        tvthunhap.setText(sttotal);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        linearNgansach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,BudgetActivity.class);
                startActivity(intent);
            }
        });
    }
}