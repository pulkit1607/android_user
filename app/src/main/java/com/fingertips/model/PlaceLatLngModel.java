package com.fingertips.model;

/**
 * Created by sahil on 3/9/16.
 */

/*JSONObject result = response.getJSONObject("result");
        JSONObject geometry = result.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        Double lat = location.getDouble("lat");
        Double lng = location.getDouble("lng");*/
public class PlaceLatLngModel {
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    Result result;

    public static class Result {
        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        Geometry geometry;

        public static class Geometry {
            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            Location location;

            public static class Location {
                double lat;
                double lng;

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }
            }
        }
    }
}
