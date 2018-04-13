package com.fingertips.app;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.squareup.okhttp.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Deepanshu on 10/22/2017.
 */

public class Constants {

    public static final String json = "json";
    public static final String WHICH = "which";


    public class Navigation{

        public static final int PROFILE = 1;
        public static final int ORDER = 2;



    }
    public class LoginCredentials{

    }

    public static class OKHTTP {
        public static final long CONNECTION_TIMEOUT = 120;//>90 seconds
        public static final long READ_TIMEOUT = 120;//>60 seconds
        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        public static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
        public static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
        public static final MediaType MEDIA_TYPE_MP3 = MediaType.parse("audio/mpeg3");
    }

    public static class RequestTags {

        public static final String PLACE_LAT_LNG = "place_lat_lng";
        public static final String PLACES_AUTOCOMPLETE = "places_autocomplete";
    }

    public class FragmentTag{
        public static final String homeFragment = "homeFragment";
        public static final String restaurantFragment  = "restaurantFragment";

    }

    public static boolean isValidMobileNo(String number) {
        String NUMBER_PATTERN = "^[7-9][0-9]{9}$";
        Pattern pattern = Pattern.compile(NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public class Extras{
        public static final String JSON  = "json";
        public static final String WHICH = "which";
        public static final String GOOGLE_API_KEY = "AIzaSyC0FFNeShY3sMw6VqtVhISLqmc4cuo8crk";
        public static final String PLACE_ID = "placeId";
        public static final String PLACE_NAME = "placeName";
        public static final int PICKUP_PLACE_ACTIVITY_REQUEST_CODE = 100;
        public static final int PICKUP = 1;
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final int PLACE_ACTIVITY_RESULT = 102;
        public static final String DATA = "data";
        public static final String email = "email";
        public static final String password = "password";
        public static final String name  = "name";
        public static final String number = "number";


    }

    public class RequestTag{
        public static final String LOGIN  = "login";
        public static final String SIGNUP = "signup";
        public static final String CATEGORY_LIST = "categoryList";
        public static final String GET_PROFILE = "profileData";
        public static final String GET_ORDERS = "getOrders";


    }

    public class Url{
        public static final String BASE_URL= "http://18.216.190.26/";

        public static final String LOGIN_URL = BASE_URL+  "v1/login/";
        public static final String SIGNUP_URL = BASE_URL+ "signup/user";
        public static final String CATEGORY_LIST_URL = BASE_URL + "categorys/";
        public static final String GET_PROFILE_URL = BASE_URL + "v1/orders/";
        public static final String GET_ORDERS_URL = BASE_URL + "v1/user/details/";


    }

    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        String location = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
        } catch (IOException e) {
            Log.e("","");
        }
        if (addresses != null && addresses.size() > 0) {

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String  postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            StringBuilder str = new StringBuilder();
            str.append(address);
            location = address+","+city+","+state+","+postalCode;

        }

        return location;
    }
}
