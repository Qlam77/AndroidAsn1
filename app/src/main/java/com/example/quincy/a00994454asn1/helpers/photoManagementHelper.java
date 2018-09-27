package com.example.quincy.a00994454asn1.helpers;

import java.util.ArrayList;

/**
 * Photo management helper that supports the functionality of the app
 * Methods here will return a boolean to tell if the method was successful or unsuccessful
 */

public class photoManagementHelper implements photoManagement{

    //save an item
    public boolean save() {
        //Take caption and save to assigned picture
        return false;
    }

    //remove an item
    public boolean remove() {
        //Remove specific photo
        return false;
    }

    //update an item
    public boolean update() {
        //Update the specific photo
        return false;
    }

    //Overload find using time
    public boolean find(long time) {
        //Find by specific time
        return false;
    }

    //Overload find using search area coordinates
    public boolean find(long lo, long lat) {
        //Find by long, lat
        return false;
    }

    //Overload find using keywords
    public boolean find(ArrayList<String> words, String request) {

        //Get requested text and see if it's in data, return something by result
        if (words.contains(request)) {
            return true;
        }

        return false;
    }
}
