package com.example.moneymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class Expense extends AppCompatActivity {
    String [] myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        myList= getResources().getStringArray(R.array.items);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.drop_down_item,myList);
        final AutoCompleteTextView autoCompleteTextView=findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);
    }
}