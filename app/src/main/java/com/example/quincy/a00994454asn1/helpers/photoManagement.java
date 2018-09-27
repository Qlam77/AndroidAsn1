package com.example.quincy.a00994454asn1.helpers;

import java.util.ArrayList;

public interface photoManagement {
    //save an item
    public boolean save();

    //remove an item
    public boolean remove();

    //update an item
    public boolean update();

    //Overload find using time
    public boolean find(long time);

    //Overload find using search area coordinates
    public boolean find(long lo, long lat);

    //Overload find using keywords
    public boolean find(ArrayList<String> words, String request);
}
