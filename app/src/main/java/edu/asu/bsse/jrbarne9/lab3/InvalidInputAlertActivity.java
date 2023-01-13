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
 * Purpose: Activity to alert the user of invalid input ranges or values.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;


import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InvalidInputAlertActivity extends AppCompatActivity {

    private boolean errorLat;
    private boolean errorLong;
    private boolean errorEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid_input_alert);
        Intent intent = getIntent();

        errorLat = intent.getBooleanExtra("latitudeError", false);
        errorLong = intent.getBooleanExtra("longitudeError", false);
        errorEmpty = intent.getBooleanExtra("emptyError", false);

        if(errorEmpty) {
            findViewById(R.id.alert_empty_text_view).setVisibility(VISIBLE);
        } else {
            if (errorLat) {
                findViewById(R.id.alert_lat_text_view).setVisibility(VISIBLE);
            }

            if (errorLong) {
                findViewById(R.id.alert_long_text_view).setVisibility(VISIBLE);
            }
        }
    }

    public void buttonClicked(View view) {finish();}
}