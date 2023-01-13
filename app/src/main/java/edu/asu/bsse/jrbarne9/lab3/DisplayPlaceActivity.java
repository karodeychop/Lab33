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
 * Purpose: Activity for displaying the details of a place selected by the user.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView placeName;
    private TextView placeCategory;
    private TextView placeElevation;
    private TextView placeLatitude;
    private TextView placeLongitude;
    private TextView placeAddressStreet;
    private TextView placeAddressTitle;
    private TextView placeDescription;
    private EditText placeNameEdit;
    private EditText placeCategoryEdit;
    private EditText placeElevationEdit;
    private EditText placeLatitudeEdit;
    private EditText placeLongitudeEdit;
    private EditText placeAddressStreetEdit;
    private EditText placeAddressTitleEdit;
    private EditText placeDescriptionEdit;

    ArrayList<TextView> textViews;
    ArrayList<EditText> editViews;

    private Place toDisplay;
    private boolean errorLat;
    private boolean errorLong;
    private boolean errorEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Place Description");
        setContentView(R.layout.activity_place_display);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.display_place_activity_toolbar);

        textViews = new ArrayList<TextView>();
        editViews = new ArrayList<EditText>();
        errorLat = false;
        errorLong = false;

        getSupportActionBar().getCustomView().findViewById(R.id.delete_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("action", "delete");
                intent.putExtra("place", toDisplay);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getSupportActionBar().getCustomView().findViewById(R.id.edit_place).setOnClickListener(new View.OnClickListener() {
            @Override   //hide textviews, fill edit texts with text view values and show them
            public void onClick(View v) {
                for(int i = 0; i < textViews.size(); i++) {
                    textViews.get(i).setVisibility(INVISIBLE);
                    editViews.get(i).setText(textViews.get(i).getText().toString());
                    editViews.get(i).setVisibility(VISIBLE);
                }

                findViewById(R.id.edit_complete_button).setVisibility(VISIBLE);
            }
        });

        placeName = findViewById(R.id.place_name_text);
        placeCategory = findViewById(R.id.place_category_text);
        placeElevation = findViewById(R.id.place_elevation_text);
        placeLatitude = findViewById(R.id.place_latitude_text);
        placeLongitude = findViewById(R.id.place_longitude_text);
        placeAddressStreet = findViewById(R.id.place_address_street_text);
        placeAddressTitle = findViewById(R.id.place_address_title_text);
        placeDescription = findViewById(R.id.place_description_text);

        textViews.add(placeName);
        textViews.add(placeCategory);
        textViews.add(placeElevation);
        textViews.add(placeLatitude);
        textViews.add(placeLongitude);
        textViews.add(placeAddressStreet);
        textViews.add(placeAddressTitle);
        textViews.add(placeDescription);

        Intent intent = getIntent();
        toDisplay = (Place) intent.getSerializableExtra("place");

        android.util.Log.d(this.getClass().getSimpleName(), "Displaying " + toDisplay.getName());

        placeName.setText(toDisplay.getName());
        placeCategory.setText(toDisplay.getCategory());
        placeElevation.setText(toDisplay.getElevation().toString());
        placeLatitude.setText(toDisplay.getLatitude().toString());
        placeLongitude.setText(toDisplay.getLongitude().toString());
        placeAddressStreet.setText(toDisplay.getAddressStreet());
        placeAddressTitle.setText(toDisplay.getAddressTitle());
        placeDescription.setText(toDisplay.getDescription());

        placeNameEdit = findViewById(R.id.place_name_text_edit);
        placeCategoryEdit = findViewById(R.id.place_category_text_edit);
        placeElevationEdit = findViewById(R.id.place_elevation_text_edit);
        placeLatitudeEdit = findViewById(R.id.place_latitude_text_edit);
        placeLongitudeEdit = findViewById(R.id.place_longitude_text_edit);
        placeAddressStreetEdit = findViewById(R.id.place_address_street_text_edit);
        placeAddressTitleEdit = findViewById(R.id.place_address_title_text_edit);
        placeDescriptionEdit = findViewById(R.id.place_description_text_edit);

        editViews.add(placeNameEdit);
        editViews.add(placeCategoryEdit);
        editViews.add(placeElevationEdit);
        editViews.add(placeLatitudeEdit);
        editViews.add(placeLongitudeEdit);
        editViews.add(placeAddressStreetEdit);
        editViews.add(placeAddressTitleEdit);
        editViews.add(placeDescriptionEdit);

        for(EditText e : editViews) {
            e.setOnClickListener(this::onClick);
        }
    }

    public void backButtonPressed(View view) {
        finish();
    }

    public void doneButtonPressed(View view) {
        toDisplay.setName(placeNameEdit.getText().toString());
        toDisplay.setCategory(placeCategoryEdit.getText().toString());
        toDisplay.setAddressStreet(placeAddressStreetEdit.getText().toString());
        toDisplay.setAddressTitle(placeAddressTitleEdit.getText().toString());
        toDisplay.setDescription(placeDescriptionEdit.getText().toString());
        toDisplay.setElevation(Double.valueOf(placeElevationEdit.getText().toString()));
        toDisplay.setLatitude(Double.valueOf(placeLatitudeEdit.getText().toString()));
        toDisplay.setLongitude(Double.valueOf(placeLongitudeEdit.getText().toString()));

        if(checkInputs()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("action", "edit");
            intent.putExtra("place", toDisplay);
            intent.putExtra("oldName", placeName.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), InvalidInputAlertActivity.class);
            intent.putExtra("latitudeError", errorLat);
            intent.putExtra("longitudeError", errorLong);
            intent.putExtra("emptyError", errorEmpty);
            startActivity(intent);
            errorLat = false;
            errorLong = false;
        }
    }

    private boolean checkInputs() {
        if(placeLatitudeEdit.getText().toString().equals("") || placeLongitudeEdit.getText().toString().equals("")) {
            errorEmpty = true;
        } else {
            Double enteredLat = Double.valueOf(placeLatitudeEdit.getText().toString());
            Double enteredLong = Double.valueOf(placeLongitudeEdit.getText().toString());

            if (enteredLat < -90.0 || enteredLat > 90.00) {
                errorLat = true;
            }

            if (enteredLong < -180.00 || enteredLong > 180.00) {
                errorLong = true;
            }
        }

        if(errorLat || errorLong || errorEmpty) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        findViewById(v.getId()).requestFocus();
        android.util.Log.d("onclick", "Requesting focus!");
    }

}