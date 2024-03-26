package com.example.packpal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.packpal.Adapter.Adapter;
import com.example.packpal.Constants.Constants;
import com.example.packpal.Data.AppData;
import com.example.packpal.Database.RoomDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<String> titles;

    List<Integer> images;

    Adapter adapter;

    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recycler_view);
        addAllTitles();
        addAllImages();
        persistAppData();
        database = RoomDB.getInstance(this);
        System.out.println("--------------------------->"+database.mainDao().getAllSelected(false).get(0).getItemname());


        adapter = new Adapter(this,titles,images,MainActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void persistAppData(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences sharedPreferences = this.getSharedPreferences(
//                "your_preference_file_key", Context.MODE_PRIVATE);

//        Or

        //SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        database = RoomDB.getInstance(this);
        AppData appData = new AppData(database);
        int last = sharedPreferences.getInt(AppData.LAST_VERSION, 0);
        if(!sharedPreferences.getBoolean(Constants.FIRST_TIME_CAMEL_CASE, false)){
            appData.persistAllData();
            editor.putBoolean(Constants.FIRST_TIME_CAMEL_CASE, true);
            editor.commit();
        } else if (last<AppData.NEW_VERSION) {

            database.mainDao().deleteAllSystemItems(Constants.SYSTEM_SMALL);
            appData.persistAllData();
            editor.putInt(AppData.LAST_VERSION, AppData.NEW_VERSION);
            editor.commit();
            
        }

    }

    private void addAllTitles(){
        titles = new ArrayList<>();
        titles.add(Constants.BASIC_NEEDS_CAMEL_CASE);
        titles.add(Constants.PERSONAL_CARE_CAMEL_CASE);
        titles.add(Constants.CLOTHING_CAMEL_CASE);
        titles.add(Constants.BABY_NEEDS_CAMEL_CASE);
        titles.add(Constants.HEALTH_CAMEL_CASE);
        titles.add(Constants.TECHNOLOGY_CAMEL_CASE);
        titles.add(Constants.FOOD_CAMEL_CASE);
        titles.add(Constants.BEACH_SUPPLIES_CAMEL_CASE);
        titles.add(Constants.CAR_SUPPLIES_CAMEL_CASE);
        titles.add(Constants.NEEDS_CAMEL_CASE);
        titles.add(Constants.MY_LIST_CAMEL_CASE);
        titles.add(Constants.MY_SELECTIONS_CAMEL_CASE);

    }

    private void addAllImages(){
        images = new ArrayList<>();
        images.add(R.drawable.basic_needs);
        images.add(R.drawable.personal_care);
        images.add(R.drawable.clothing);
        images.add(R.drawable.baby);
        images.add(R.drawable.medical);
        images.add(R.drawable.technology);
        images.add(R.drawable.food);
        images.add(R.drawable.beach);
        images.add(R.drawable.car);
        images.add(R.drawable.needs);
        images.add(R.drawable.my_list);
        images.add(R.drawable.selected);

    }
}