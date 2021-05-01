package com.expandablelistview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListedListener{

    ExpandableListView expandList;
    ExpandListAdapter listAdapter;
    List<String> listDataHeader;
    List<String> temp;
    HashMap<String, List<String>> listDataChild;
    Button BtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandList = findViewById(R.id.expList);
        BtnSave = findViewById(R.id.btnsave);
        prepareListData();
        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        listAdapter.setmListener(this);
        expandList.setAdapter(listAdapter);


        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ("You have selected: " + temp.toString()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getResources().getString(R.string.str_pets));
        listDataHeader.add(getResources().getString(R.string.str_fruits));

        List<String> petsList = Arrays.asList(getResources().getStringArray(R.array.pets_array));
        List<String> fruitsList  = Arrays.asList(getResources().getStringArray(R.array.fruits_array));

        listDataChild.put(listDataHeader.get(0), petsList);
        listDataChild.put(listDataHeader.get(1), fruitsList);
    }

    @Override
    public void onListChanged(final ArrayList<String> chosenChildren) {
        temp = chosenChildren;
    }
}
