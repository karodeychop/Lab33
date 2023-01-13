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
 * Purpose: This is an activity to add a place to the database.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AddPlaceActivity extends AppCompatActivity {

    private EditText placeNameAdd;
    private EditText placeCategoryAdd;
    private EditText placeElevationAdd;
    private EditText placeLatitudeAdd;
    private EditText placeLongitudeAdd;
    private EditText placeAddressStreetAdd;
    private EditText placeAddressTitleAdd;
    private EditText placeDescriptionAdd;

    private boolean errorLat;
    private boolean errorLong;
    private boolean errorEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.activity_place_add_toolbar);

        getSupportActionBar().getCustomView().findViewById(R.id.submit_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place toAdd;

                if(checkInputs()) {

                    toAdd = new Place(placeNameAdd.getText().toString(),
                            placeDescriptionAdd.getText().toString(),
                            placeCategoryAdd.getText().toString(),
                            placeAddressStreetAdd.getText().toString(),
                            placeAddressTitleAdd.getText().toString(),
                            Double.valueOf(placeLatitudeAdd.getText().toString()),
                            Double.valueOf(placeLongitudeAdd.getText().toString()),
                            Double.valueOf(placeElevationAdd.getText().toString()));

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("action", "add");
                    intent.putExtra("place", toAdd);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), InvalidInputAlertActivity.class);
                    intent.putExtra("latitudeError", errorLat);
                    intent.putExtra("longitudeError", errorLong);
                    intent.putExtra("emptyError", errorEmpty);
                    startActivity(intent);
                    errorLat = false;
                    errorLong = false;
                }
            }
        });

        getSupportActionBar().getCustomView().findViewById(R.id.cancel_add_place).setOnClickListener(v -> finish());

        placeNameAdd = findViewById(R.id.place_name_text_add);
        placeNameAdd.setText("");
        placeCategoryAdd = findViewById(R.id.place_category_text_add);
        placeCategoryAdd.setText("");
        placeElevationAdd = findViewById(R.id.place_elevation_text_add);
        placeElevationAdd.setText("");
        placeLatitudeAdd = findViewById(R.id.place_latitude_text_add);
        placeLatitudeAdd.setText("");
        placeLongitudeAdd = findViewById(R.id.place_longitude_text_add);
        placeLongitudeAdd.setText("");
        placeAddressStreetAdd = findViewById(R.id.place_address_street_text_add);
        placeAddressStreetAdd.setText("");
        placeAddressTitleAdd = findViewById(R.id.place_address_title_text_add);
        placeAddressTitleAdd.setText("");
        placeDescriptionAdd = findViewById(R.id.place_description_text_add);
        placeDescriptionAdd.setText("");

        errorLat = false;
        errorLong = false;
        errorEmpty = false;
    }

    private boolean checkInputs() {
        if(placeLatitudeAdd.getText().toString().equals("") || placeLongitudeAdd.getText().toString().equals("")) {
            errorEmpty = true;
        } else {
            Double enteredLat = Double.valueOf(placeLatitudeAdd.getText().toString());
            Double enteredLong = Double.valueOf(placeLongitudeAdd.getText().toString());

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
}