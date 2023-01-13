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
 * Purpose: This is a class to represent and modify a place in the app.
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import org.json.JSONObject;

import java.io.Serializable;

public class Place implements Serializable {

    private static boolean debug = true;

    private String name;
    private String description;
    private String category;
    private String addressStreet;
    private String addressTitle;
    private Double latitude;
    private Double longitude;
    private Double elevation;

    public Place(String json) {
        try{
            debugMessage("Creating new Place object from json...");
            JSONObject place = new JSONObject(json);
            this.name = place.getString("name");
            this.description = place.getString("description");
            this.category = place.getString("category");
            this.addressTitle = place.optString("address-title");
            if(this.addressTitle.isEmpty()) {
                this.addressTitle = "N/A";
            }
            this.addressStreet = place.getString("address-street");
            this.latitude = place.getDouble("latitude");
            this.longitude = place.getDouble("longitude");
            this.elevation = place.getDouble("elevation");


        } catch(Exception e) {
            android.util.Log.d(this.getClass().getSimpleName(), "Error occurred when creating new Place object! Please check json string!");
        }
    }

    public Place (String name, String description, String category, String addressStreet, String addressTitle, Double latitude, Double longitude, Double elevation) {
        debugMessage("Creating new Place object...");
        this.name = name;
        this.description = description;
        this.category = category;
        this.addressStreet = addressStreet;
        if(addressTitle.isEmpty()) {
            this.addressTitle = "N/A";
        } else {
            this.addressTitle = addressTitle;
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCategory() {
        return this.category;
    }

    public String getAddressStreet() {
        return this.addressStreet;
    }

    public String getAddressTitle() {
        return this.addressTitle;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double getElevation() {
        return this.elevation;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    public void setAddressStreet(String newAddressSt) {
        this.addressStreet = newAddressSt;
    }

    public void setAddressTitle(String newAddressTitle) {
        this.addressTitle = newAddressTitle;
    }

    public void setLatitude(Double newLat) {
        this.latitude = newLat;
    }

    public void setLongitude(Double newLong) {
        this.longitude = newLong;
    }

    public void setElevation(Double newElev) {
        this.elevation = newElev;
    }

    public JSONObject toJson() {
        debugMessage("Creating JSONObject from place...");
        JSONObject placeJson = new JSONObject();
        try {
            placeJson.put("name", this.name);
            placeJson.put("description", this.description);
            placeJson.put("category", this.category);
            placeJson.put("address-street", this.addressStreet);
            placeJson.put("address-title", this.addressTitle);
            placeJson.put("elevation", this.elevation);

        }catch (Exception e) {
            android.util.Log.d(this.getClass().getSimpleName(), "Exception occurred while making Place object a json object!");
        }

        return placeJson;
    }

    public String toString() {
        debugMessage("Creating String from Place...");
        StringBuilder string = new StringBuilder();
        string.append("Name:           ").append(this.name).append("\n");
        string.append("Description:    ").append(this.description).append("\n");
        string.append("Category:       ").append(this.category).append("\n");
        string.append("Address Title:  ").append(this.addressTitle).append("\n");
        string.append("Address Street: ").append(this.addressStreet).append("\n");
        string.append("Longitude:      ").append(this.longitude.toString()).append("\n");
        string.append("Latitude:       ").append(this.latitude.toString()).append("\n");
        string.append("Elevation:      ").append(this.elevation.toString()).append("\n");

        return string.toString();
    }

    private void debugMessage(String message) {
        if(debug) {
            android.util.Log.d(this.getClass().getSimpleName(), message);
        }
    }
}