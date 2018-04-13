package com.fingertips.model;

import java.util.List;

/**
 * Created by sahil on 2/9/16.
 */
public class PlacesModel {
    private List<PlaceAutoComplete> predictions;

    public List<PlaceAutoComplete> getPlaces() {
        return predictions;
    }

    public void setPlaces(List<PlaceAutoComplete> places) {
        this.predictions = places;
    }

    public static class PlaceAutoComplete {
        private String place_id;
        private String description;

        public String getPlaceDesc() {
            return description;
        }

        public void setPlaceDesc(String placeDesc) {
            description = placeDesc;
        }

        public String getPlaceID() {
            return place_id;
        }

        public void setPlaceID(String placeID) {
            place_id = placeID;
        }
    }
}
