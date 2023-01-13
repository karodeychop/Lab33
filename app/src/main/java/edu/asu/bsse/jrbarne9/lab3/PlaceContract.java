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
 * Purpose: This is a class to represent the table(s) in the database.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import android.provider.BaseColumns;

public final class PlaceContract {
    private PlaceContract(){}

    public static class PlaceTable implements BaseColumns {
        public static final String TABLE_NAME = "placeDescriptions";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ADDRESS_STREET = "addressStreet";
        public static final String COLUMN_NAME_ADDRESS_TITLE = "addressTitle";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ELEVATION = "elevation";
    }
}