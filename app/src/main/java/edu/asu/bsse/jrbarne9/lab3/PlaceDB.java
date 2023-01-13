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
 * Purpose: This is a class to create, access, and modify a database of json places.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.MainThread;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlaceDB extends SQLiteOpenHelper implements Serializable{

    private static final boolean debug = true;
    private static final int DATABASE_VERSION = 4;
    private static final String name = "places.db";
    private SQLiteDatabase placeDB;
    private Context context;

    private static final String CREATE_TABLE = "CREATE TABLE " + PlaceContract.PlaceTable.TABLE_NAME + " ("
            + PlaceContract.PlaceTable.COLUMN_NAME_NAME + " TEXT PRIMARY KEY, "
            + PlaceContract.PlaceTable.COLUMN_NAME_CATEGORY + " TEXT, "
            + PlaceContract.PlaceTable.COLUMN_NAME_DESCRIPTION + " TEXT, "
            + PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_STREET + " TEXT, "
            + PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_TITLE + " TEXT, "
            + PlaceContract.PlaceTable.COLUMN_NAME_LATITUDE + " DOUBLE, "
            + PlaceContract.PlaceTable.COLUMN_NAME_LONGITUDE + " DOUBLE, "
            + PlaceContract.PlaceTable.COLUMN_NAME_ELEVATION + " DOUBLE)";

    private static final String CREATE_ENTRIES = "INSERT INTO " + PlaceContract.PlaceTable.TABLE_NAME + "("
            + PlaceContract.PlaceTable.COLUMN_NAME_NAME + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_CATEGORY + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_DESCRIPTION + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_STREET + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_TITLE + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_LATITUDE + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_LONGITUDE + ", "
            + PlaceContract.PlaceTable.COLUMN_NAME_ELEVATION + ") VALUES " +
            "('ASU-West','School','Home of ASU''s Applied Computing Program','13591 N 47th Ave$Pheonix AZ 85051','ASU West Campus',33.608979,-112.159469,1100.0)," +
            "('UAK-Anchorage','School','University of Alaska''s largest campus','290 Spirit Dr$Anchorage AK 99508','University of Alaska at Anchorage',61.189748,-149.826721,0.0)," +
            "('Toreros','School','The University of San Diego, a private Catholic undergraduate university.','5998 Alcala Park$San Diego CA 92110','University of San Diego',32.771923,-117.188204,200.0)," +
            "('Barrow-Alaska','Travel','The only real way in and out of Barrow Alaska.','1725 Ahkovak St$Barrow AK 99723','Will Rogers Airport',71.287881,-156.779417,5.0)," +
            "('Calgary-Alberta','Travel','The home of the Calgary Stampede Celebration','2000 Airport Rd NE$Calgary AB T2E 6Z8 Canada','Calgary International Airport',51.131377,-114.011246,3556.0)," +
            "('London-England','Travel','Renaissance Hotel at the Heathrow Airport','5 Mondial Way$Harlington Hayes UB3 UK','Renaissance London Heathrow Airport',51.481959,-0.445286,82.0)," +
            "('Moscow-Russia','Travel','The Marriott Courtyard in downtown Moscow','Voznesenskiy per 7 $ Moskva Russia 125009','Courtyard Moscow City Center',55.758666,37.604058,512.0)," +
            "('New-York-NY','Travel','New York City Hall at West end of Brooklyn Bridge','1 Elk St$New York NY 10007','New York City Hall',-74.005948,40.712991,2.0)," +
            "('Notre-Dame-Paris','Travel','The 13th century cathedral with gargoyles, one of the first flying buttress, and home of the purported crown of thorns.','6 Parvis Notre-Dame Pl Jean-Paul-II$75004 Paris France','Cathedral Notre Dame de Paris',48.852972, 2.349910, 115.0)," +
            "('Circlestone','Hike','Indian Ruins located on the second highest peak in the Superstition Wilderness of the Tonto National Forest. Leave Fireline at Turney Spring (33.487610,-111.132400)','','', 33.477524, -111.134345, 6000.0)," +
            "('Reavis-Ranch','Hike','Historic Ranch in Superstition Mountains famous for Apple orchards','','', 33.491154, -111.155385, 5000.0)," +
            "('Rogers-Trailhead','Hike','Trailhead for hiking to Rogers Canyon Ruins and Reavis Ranch','','', 33.422212, -111.173393, 4500.0)," +
            "('Reavis-Grave','Hike','Grave site of Reavis Ranch Proprietor.','','', 33.441499, -111.182511, 3900.0)," +
            "('Muir-Woods','Hike','Redwood forest North of San Francisco, surrounded by Mount Tamalpais State Park.','1 Muir Woods Rd$Mill Valley CA 94941','Muir Woods National Monument', 37.8912, -122.5957, 350.0)," +
            "('Windcave-Peak','Hike','Beyond the Wind Cave is a half mile trail with 250'' additional elevation to the peak overlooking Usery and the Valley.','3939 N Usery Pass Rd$Mesa AZ 85207','Usery Mountain Recreation Area', 33.476069, -111.595709, 3130.0)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + PlaceContract.PlaceTable.TABLE_NAME;


    private HashMap<String, Place> places;

    public PlaceDB(Context context) {
        super(context, name, null, DATABASE_VERSION);
        this.context = context;
        places = new HashMap<String, Place>();

    }

    public boolean addPlace(Place place) {
        boolean success = true;
        placeDB = getWritableDatabase();
        if(places.containsKey(place.getName())) {
            if(replacePlace(place, place.getName())) {
                success = true;
            } else {
                success = false;
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_NAME, place.getName());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_CATEGORY, place.getCategory());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_DESCRIPTION, place.getDescription());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_TITLE, place.getAddressTitle());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_STREET, place.getAddressStreet());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_ELEVATION, place.getElevation());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_LATITUDE, place.getLatitude());
            values.put(PlaceContract.PlaceTable.COLUMN_NAME_LONGITUDE, place.getLongitude());

            long id = placeDB.insert(
                    PlaceContract.PlaceTable.TABLE_NAME,
                    null,
                    values
            );

            if(id == -1) {
                success = false;
            }
        }

        return success;
    }

    public boolean replacePlace(Place place, String oldName) {
        boolean success = true;

        if(!removePlace(oldName)) {
            success = false;
        } else {
            if(!addPlace(place)) {
                success = false;
            }
        }

        return success;
    }

    public boolean removePlace(String placeName) {
        boolean success = true;
        placeDB = getWritableDatabase();

        String selection = PlaceContract.PlaceTable.COLUMN_NAME_NAME + " LIKE ?";
        String[] selectionArgs = {placeName};

        int deletedRows = placeDB.delete(
                PlaceContract.PlaceTable.TABLE_NAME,
                selection,
                selectionArgs
        );

        if(deletedRows == 0) {
            success = false;
        }

        return success;
    }

    //returns distance in kilometers
    public static Double calculateDistance(Place p1, Place p2) {
        double R = 6371e3;
        double radLat1 = p1.getLatitude() * Math.PI/180;
        double radLat2 = p2.getLatitude() * Math.PI/180;
        double deltaLat = (p2.getLatitude() - p1.getLatitude()) * Math.PI/180;
        double deltaLon = (p2.getLongitude() - p1.getLongitude()) * Math.PI/180;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceMeters = R * c;

        if(debug) {
            debugMessage("Distance calculated between " + p1.getName() + " and " + p2.getName() + ": " + distanceMeters/1000 + " kilometers.");
        }

        return distanceMeters / 1000;
    }

    public static Double calculateBearing(Place p1, Place p2) {
        double radLat1 = p1.getLatitude() * Math.PI/180;
        double radLat2 = p2.getLatitude() * Math.PI/180;
        double deltaLon = (p2.getLongitude() - p1.getLongitude()) * Math.PI/180;
        double y = Math.sin(deltaLon) * Math.cos(radLat2);
        double x = Math.cos(radLat1) * Math.sin(radLat2) - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(deltaLon);
        double theta = Math.atan2(y, x);
        double bearingDegrees = ((theta * 180) / Math.PI + 360) % 360;

        if(debug) {
            debugMessage("Bearing calculated between " + p1.getName() + " and " + p2.getName() + ": " + bearingDegrees + " degrees.");
        }

        return bearingDegrees;
    }

    public Place getPlace(String name) {
        Place pl;
        if(!places.isEmpty() && places.containsKey(name)) {
            pl = places.get(name);
        } else {
            pl = null;
        }

        return pl;
    }

    //get a list of all entries for names in a database and return that as a list
    public List<String> getPlaceNames() {

        ArrayList<String> names = new ArrayList<String>();
        placeDB = getReadableDatabase();

        String[] projection = {
                PlaceContract.PlaceTable.COLUMN_NAME_NAME,
        };

        Cursor cursor = placeDB.query(
                PlaceContract.PlaceTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_NAME));
            names.add(name);
        }
        cursor.close();

        return names;
    }

    //get a list of all entries in database, and make place out of them then populate with their names and enter them
    public Map<String, Place> getMap() {
        this.places = new HashMap<String, Place>();
        placeDB = getReadableDatabase();
        String[] projection = {
                PlaceContract.PlaceTable.COLUMN_NAME_NAME,
                PlaceContract.PlaceTable.COLUMN_NAME_CATEGORY,
                PlaceContract.PlaceTable.COLUMN_NAME_DESCRIPTION,
                PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_TITLE,
                PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_STREET,
                PlaceContract.PlaceTable.COLUMN_NAME_LATITUDE,
                PlaceContract.PlaceTable.COLUMN_NAME_LONGITUDE,
                PlaceContract.PlaceTable.COLUMN_NAME_ELEVATION
        };

        Cursor cursor = placeDB.query(
                PlaceContract.PlaceTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_NAME));
            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_DESCRIPTION));
            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_CATEGORY));
            String addressStreet = cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_STREET));
            String addressTitle = cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_ADDRESS_TITLE));
            Double elevation = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_ELEVATION));
            Double latitude = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_LATITUDE));
            Double longitude = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PlaceContract.PlaceTable.COLUMN_NAME_LONGITUDE));
            Place toAdd = new Place(name, description, category, addressStreet, addressTitle, latitude, longitude, elevation);

            this.places.put(toAdd.getName(), toAdd);
        }
        cursor.close();

        return this.places;
    }

    private static void debugMessage(String message) {
        if(debug) {
            android.util.Log.d("PlaceDB", message);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}