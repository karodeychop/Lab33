/**
 * Copyright 2021 Jacob Barnes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: Main activity to display a selectable RecyclerView of Place objects as well as
 * providing buttons for the user to add a place or go to the DistanceBearingActivity.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.PlaceListener  {

    private RecyclerView placesView; //display place name

    private PlaceDB places;
    private ArrayList<String> placeNames;

    HashMap<String, Place> listEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.main_activity_toolbar);

        places = new PlaceDB(this);


        getSupportActionBar().getCustomView().findViewById(R.id.add_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        getSupportActionBar().getCustomView().findViewById(R.id.compare_arrows).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DistanceBearingActivity.class);
                intent.putExtra("map", listEntries);
                intent.putExtra("names", placeNames);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        placesView = findViewById(R.id.place_recycler_view);
        listEntries = (HashMap<String, Place>) places.getMap();
        placeNames = (ArrayList<String>) places.getPlaceNames();

        setAdapter();
    }

    private void setAdapter() {
        RecyclerAdapter rAdapter = new RecyclerAdapter(listEntries, placeNames, this::onPlaceClick);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        placesView.setLayoutManager(layoutManager);
        placesView.setItemAnimator(new DefaultItemAnimator());
        placesView.setAdapter(rAdapter);
    }

    public void addButton() {

    }

    public void onPlaceClick(int index) {
        if(index < placeNames.size()) { //account for dummy index at top of list for header
            Intent intent = new Intent(this, DisplayPlaceActivity.class);
            intent.putExtra("place", listEntries.get(placeNames.get(index)));
            startActivityForResult(intent, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(data != null) {
                String action = data.getStringExtra("action");
                if (action.equals("delete")) {
                    Place toRemove = (Place) data.getSerializableExtra("place");
                    if (places.removePlace(toRemove.getName())) {
                        android.util.Log.d(this.getClass().getSimpleName(), "Removed place " + toRemove.getName());
                    } else {
                        android.util.Log.d(this.getClass().getSimpleName(), "Failed to remove place " + toRemove.getName());
                    }
                } else if (action.equals("edit")) {
                    Place toReplace = (Place) data.getSerializableExtra("place");
                    if (places.replacePlace(toReplace, data.getStringExtra("oldName"))) {
                        android.util.Log.d(this.getClass().getSimpleName(), "Replaced place " + data.getStringExtra("oldName"));
                    } else {
                        android.util.Log.d(this.getClass().getSimpleName(), "Failed to replace place " + toReplace.getName());
                    }

                } else if(action.equals("add")) {
                    Place toAdd = (Place) data.getSerializableExtra("place");
                    if(places.addPlace(toAdd)) {
                        android.util.Log.d(this.getClass().getSimpleName(), "Added new place " + toAdd.getName() + ".");
                    } else {
                        android.util.Log.d(this.getClass().getSimpleName(), "Failed to add new place " + toAdd.getName() + ".");
                    }
                }
            }
        }
    }
}