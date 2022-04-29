package com.example.moneymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout linearNgansach;
    private TextView tv_homnay,tv_thunhap,tv_week,tv_month,tv_chiphi,moneytoday,moneyweek,moneymonth,tv_sodu;

    private DatabaseReference budgetRef,personalRef,expensesRef;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;

    private WeekSpendingAdapter weekSpendingAdapter;
    private List<Data> myDataList;
    private String onlineUserId = "";

    private int totalAmountMonth=0;
    private int totalAmountBudget=0;
    private int totalAmountBudgetB=0;
    private int totalAmountBudgetC=0;
    private int totalAmountBudgetD=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linearNgansach = findViewById(R.id.linearNgansach);
        tv_homnay = findViewById(R.id.tv_homnay);
        tv_week = findViewById(R.id.tv_week);
        tv_month = findViewById(R.id.tv_month);
        tv_thunhap = findViewById(R.id.tv_thunhap);
        tv_chiphi = findViewById(R.id.tv_chiphi);
        moneytoday = findViewById(R.id.moneytoday);
        moneyweek = findViewById(R.id.moneyweek);
        moneymonth = findViewById(R.id.moneymonth);
        tv_sodu = findViewById(R.id.tv_sodu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        mAuth = FirebaseAuth.getInstance();
        onlineUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserId);
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);


        myDataList = new ArrayList<>();
        weekSpendingAdapter = new WeekSpendingAdapter(HomeActivity.this, myDataList);
        recyclerView.setAdapter(weekSpendingAdapter);

        readMonthSpendingItems();

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudgetB += pTotal;
                    }
                    totalAmountBudgetC = totalAmountBudgetB;
                    personalRef.child("budget").setValue(totalAmountBudgetC);
                } else {
                    personalRef.child("budget").setValue(0);
                    Toast.makeText(HomeActivity.this, "Please Set a BUDGET", Toast.LENGTH_LONG).show();
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
        tv_homnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,TodaySpendingActivity.class);
                startActivity(intent);
            }
        });
        tv_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,WeekSpendingActivity.class);
                intent.putExtra("type","week");
                startActivity(intent);
            }
        });
        tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,WeekSpendingActivity.class);
                intent.putExtra("type","month");
                startActivity(intent);
            }
        });
    }

    private void readMonthSpendingItems() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);

        Query query = expensesRef.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataList.add(data);
                }

                weekSpendingAdapter.notifyDataSetChanged();

                int totalAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getBudgetAmount();
        getTodaySpendingAmount();
        getWeekSpendingAmount();
        getMonthSpendingAmount();
        getSavings();
    }

    private void getSavings() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int budget;
                    if(snapshot.hasChild("budget")){
                        budget=Integer.parseInt(Objects.requireNonNull(snapshot.child("budget").getValue().toString()));
                    }
                    else {
                        budget=0;
                    }
                    int monthSpending;
                    if(snapshot.hasChild("month")){
                        monthSpending=Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                    }
                    else {
                        monthSpending=0;
                    }
                    int savings=budget-monthSpending;
                    tv_sodu.setText(savings+"$");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMonthSpendingAmount() {
        MutableDateTime epoch=new MutableDateTime();
        epoch.setDate(0);
        DateTime now=new DateTime();
        Months months=Months.monthsBetween(epoch,now);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount=0;
                for(DataSnapshot ds:snapshot.getChildren()){
                    Map<String,Object> map=(Map<String, Object>) ds.getValue();
                    Object total=map.get("amount");
                    int pTotal=Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    moneymonth.setText(totalAmount+"$");
                    tv_chiphi.setText(totalAmount+"$");
                }
                personalRef.child("month").setValue(totalAmount);
                totalAmountMonth=totalAmount;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWeekSpendingAmount() {
        MutableDateTime epoch=new MutableDateTime();
        epoch.setDate(0);
        DateTime now=new DateTime();
        Weeks weeks=Weeks.weeksBetween(epoch,now);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount=0;
                for(DataSnapshot ds:snapshot.getChildren()){
                    Map<String,Object> map=(Map<String, Object>) ds.getValue();
                    Object total=map.get("amount");
                    int pTotal=Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    moneyweek.setText(totalAmount+"$");
                }
                personalRef.child("week").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTodaySpendingAmount() {
        DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal=Calendar.getInstance();
        String date=dateFormat.format(cal.getTime());
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount=0;
                for(DataSnapshot ds:snapshot.getChildren()){
                    Map<String,Object> map=(Map<String, Object>) ds.getValue();
                    Object total=map.get("amount");
                    int pTotal=Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    moneytoday.setText(totalAmount+"$");
                }
                personalRef.child("today").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBudgetAmount() {
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Map<String,Object> map=(Map<String, Object>) ds.getValue();
                        Object total=map.get("amount");
                        int pTotal=Integer.parseInt(String.valueOf(total));
                        totalAmountBudget+=pTotal;
                        tv_thunhap.setText(String.valueOf(totalAmountBudget)+"$");
                    }
                    totalAmountBudget=0;
                }else {
                    totalAmountBudget=0;
                    tv_thunhap.setText(String.valueOf(0)+"$");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}