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
 * Purpose: This is a class to provide an adapter for the RecyclerView in MainActivity
 *
 * @author Jacob Barnes (jrbarne9) mailto: jrbarne9@asu.edu
 * @version Nov 25, 2021
 */

package edu.asu.bsse.jrbarne9.lab3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private HashMap<String, Place> placesMap;
    private ArrayList<String> placeNames;
    private PlaceListener rPlaceListener;

    public RecyclerAdapter(HashMap<String, Place> places, ArrayList<String> placeNames, PlaceListener placeListener) {
        this.placesMap = places;
        this.placeNames = placeNames;
        this.rPlaceListener = placeListener;

    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View placeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        return new MyViewHolder(placeView, rPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        if(position < placeNames.size()) {
            holder.placeTV.setText(placeNames.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return placesMap.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView placeTV;
        PlaceListener placeListener;

        MyViewHolder(final View view, PlaceListener placeListener) {
            super(view);
            placeTV = view.findViewById(R.id.place_name);
            this.placeListener = placeListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            placeListener.onPlaceClick(getAdapterPosition());
        }
    }

    public interface PlaceListener {
        void onPlaceClick(int index);
    }
}