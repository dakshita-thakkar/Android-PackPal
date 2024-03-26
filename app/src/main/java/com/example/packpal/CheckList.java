package com.example.packpal;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.packpal.R;


import com.example.packpal.Adapter.CheckListAdapter;
import com.example.packpal.Constants.Constants;
import com.example.packpal.Data.AppData;
import com.example.packpal.Database.RoomDB;
import com.example.packpal.Models.Items;

import java.util.ArrayList;
import java.util.List;

public class CheckList extends AppCompatActivity {
    private static final int CHECKLIST_REQUEST_CODE = 101;

    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    RoomDB database;
    List<Items> itemsList = new ArrayList<>();
    String header, show;

    EditText txtAdd;
    Button btnAdd;

    LinearLayout linearLayout;


//    MENU BAR CODE:
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);

        if (Constants.MY_SELECTIONS.equals(header)) {
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);


        } else if (Constants.MY_LIST_CAMEL_CASE.equals(header)) {
            menu.getItem(1).setVisible(false);
        }
//            CODE FOR SEARCH BAR
            MenuItem menuItem = menu.findItem(R.id.btnSearch);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    List<Items> finalList = new ArrayList<>();
                    for(Items items:itemsList){
                        if(items.getItemname().toLowerCase().startsWith(newText.toLowerCase())){
                            finalList.add(items);
                        }
                    }
                    updateRecycler(finalList);
                    return false;
                }
            });


        return true;
    }

//    CODE FOR ADDING ALL THE ITEMS ADDED BY THE USER TO MY-SELECTIONS



//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent intent = new Intent(this, CheckList.class);
//        AppData appData = new AppData(database, this);
//
//        switch (item.getItemId()) {
//            case (R.id.btnMySelections):
//                intent.putExtra(Constants.HEADER_SMALL, Constants.MY_SELECTIONS);
//                intent.putExtra(Constants.SHOW_SMALL, Constants.FALSE_STRING);
//                startActivity(intent);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

// CODE FOR SETTING THE HEADER FOR ALL THE INDIVIDUAL MODULES:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        header = intent.getStringExtra(Constants.HEADER_SMALL);

        show = intent.getStringExtra(Constants.SHOW_SMALL);

        getSupportActionBar().setTitle(header);

//        CODE FOR INITIALIZING ALL VARIABLES
        txtAdd = findViewById(R.id.txtAdd);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linearLayout);

//        FOR DATABASE WE GET AN OBJECT/INSTANCE
        database = RoomDB.getInstance(this);


//        if the list for a particular module is empty, show only header , else get the list of items fom database
        if(Constants.FALSE_STRING.equals(show)){
            linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getAllSelected(true);
        }else{
            itemsList = database.mainDao().getAll(header);
        }

        updateRecycler(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = txtAdd.getText().toString();
                if(itemName != null && !itemName.isEmpty()){

                    addNewItem(itemName);
                    Toast.makeText(CheckList.this, "Item added successfully!", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(CheckList.this, "Please enter the item you want to add!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    CODE FOR BACK BUTTON
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    CODE TO ADD NEW CUSTOM ITEM TO THE DATABASE ENTERED BY THE USER

    private void addNewItem(String itemName){

        Items item = new Items();
        item.setChecked(false);
        item.setCategory(header);
        item.setItemname(itemName);
        item.setAddedby(Constants.USER_SMALL);
        database.mainDao().saveItem(item);
        itemsList = database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkListAdapter.getItemCount()-1);
        txtAdd.setText("");

    }


//    CODE TO UPDATE THE RECYCLER VIEW

    private void updateRecycler(List<Items> itemsList){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkListAdapter = new CheckListAdapter(CheckList.this, itemsList,database,show);
        recyclerView.setAdapter(checkListAdapter);

    }
}