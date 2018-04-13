package com.fingertips.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Time Traveller
 */

public class XClass {

    private Context cT;

    public XClass(Context cT) {
        this.cT = cT;
    }

    public void saveInSharedPref(int stringName, String type, String elementData) {
        // The value of elementData is String because it can later be parsed into corresponding types.

        SharedPreferences sP = cT.getSharedPreferences(cT.getString(stringName), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();

        try {
            if (type.equalsIgnoreCase("int")) {
                editor.putInt(cT.getString(stringName), Integer.parseInt(elementData));
            } else if (type.equalsIgnoreCase("String")) {
                editor.putString(cT.getString(stringName), elementData);
            } else if (type.equalsIgnoreCase("Long")) {
                editor.putLong(cT.getString(stringName), Long.parseLong(elementData));
            } else if (type.equalsIgnoreCase("Float")) {
                editor.putFloat(cT.getString(stringName), Float.parseFloat(elementData));
            } else if (type.equalsIgnoreCase("Boolean")) {
                editor.putBoolean(cT.getString(stringName), Boolean.parseBoolean(elementData));
            }
        } catch (NumberFormatException gotcha) {
            gotcha.printStackTrace();
        }

        editor.apply();
    }

    public Object getFromSharedPref(int stringName, String type, String defaultVal) {
        // The value of elementData is String because it can later be parsed into corresponding types.

        SharedPreferences sP = cT.getSharedPreferences(cT.getString(stringName), Context.MODE_PRIVATE);
        try {
            if (type.equalsIgnoreCase("int")) {
                return sP.getInt(cT.getString(stringName), Integer.parseInt(defaultVal));
            } else if (type.equalsIgnoreCase("String")) {
                return sP.getString(cT.getString(stringName), defaultVal);
            } else if (type.equalsIgnoreCase("Long")) {
                return sP.getLong(cT.getString(stringName), Long.parseLong(defaultVal));
            } else if (type.equalsIgnoreCase("Float")) {
                return sP.getFloat(cT.getString(stringName), Float.parseFloat(defaultVal));
            } else if (type.equalsIgnoreCase("Boolean")) {
                return sP.getBoolean(cT.getString(stringName), Boolean.parseBoolean(defaultVal));
            } else {
                return 0;
            }
        } catch (NumberFormatException gotcha) {
            gotcha.printStackTrace();
            return 0;
        }
    }
}
