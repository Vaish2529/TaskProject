package com.example.taskproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.taskproject.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding activityMainBinding;
    private static final String TAG = "MainActivity";
    private ArrayList<ListData> listData = new ArrayList<>();
    private ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        activityMainBinding.submit.setOnClickListener(this);
        showData();
        loadData();

    }

    private void showData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("data", null);
        Type type = new TypeToken<ArrayList<ListData>>() {}.getType();
        listData = gson.fromJson(json, type);
        if (listData == null) {
            listData = new ArrayList<>();
        }
    }

    private void loadData() {
        listAdapter = new ListAdapter(listData, MainActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        activityMainBinding.recyclerView.setHasFixedSize(true);
        activityMainBinding.recyclerView.setLayoutManager(manager);
        activityMainBinding.recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
         if (activityMainBinding.submit == v){
             if (validate()){
                 listData.add(new ListData(activityMainBinding.mail.getText().toString(), activityMainBinding.number.getText().toString()));
                 listAdapter.notifyItemInserted(listData.size());
                 loadData();
                 saveData();
             }
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listData);
        editor.putString("data", json);
        editor.apply();
        Toast.makeText(this, "Saved Data to Shared preferences. ", Toast.LENGTH_SHORT).show();

    }

    private boolean validate() {
        if (activityMainBinding.mail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(activityMainBinding.mail.getText().toString()).matches()){
            Toast.makeText(this, "Please Enter valid email", Toast.LENGTH_SHORT).show();
        }else if (activityMainBinding.number.getText().toString().isEmpty() || activityMainBinding.number.getText().toString().length()<10){
            Toast.makeText(this, "Please Enter valid Number", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}