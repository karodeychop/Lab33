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
 * Purpose: Activity for calculating the distance and initial bearing between two user selected places
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DistanceBearingActivity extends AppCompatActivity {

    private TextView distanceText;
    private TextView bearingText;
    private NumberPicker place1;
    private NumberPicker place2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_bearing);

        distanceText = findViewById(R.id.distanceTextView);
        bearingText = findViewById(R.id.bearingTextView);
        place1 = findViewById(R.id.numberPicker1);
        place2 = findViewById(R.id.numberPicker2);

        Intent intent = getIntent();
        HashMap<String, Place> placesHashMap = (HashMap<String, Place>) intent.getSerializableExtra("map");
        ArrayList<String> names = (ArrayList<String>) intent.getSerializableExtra("names");

        String[] displayValues = new String[names.size()];

        for(int i = 0; i < names.size(); i++) {
            displayValues[i] = names.get(i);
        }


        place1.setMaxValue(names.size() - 1);
        place2.setMaxValue(names.size() - 1);
        place1.setMinValue(0);
        place2.setMinValue(0);
        place1.setDisplayedValues(displayValues);
        place2.setDisplayedValues(displayValues);

        place1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Place p1 = placesHashMap.get(names.get(newVal));
                Place p2 = placesHashMap.get(names.get(place2.getValue()));

                Double distance = PlaceDB.calculateDistance(p1, p2);
                Double bearing = PlaceDB.calculateBearing(p1, p2);

                distanceText.setText(distance.toString() + " KM");
                bearingText.setText(bearing.toString() + " Degrees");
            }
        });

        place2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Place p2 = placesHashMap.get(names.get(newVal));
                Place p1 = placesHashMap.get(names.get(place1.getValue()));

                Double distance = PlaceDB.calculateDistance(p1, p2);
                Double bearing = PlaceDB.calculateBearing(p1, p2);

                distanceText.setText(distance.toString() + " KM");
                bearingText.setText(bearing.toString() + " Degrees");
            }
        });

    }
}