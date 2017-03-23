package lazy.hackthon.com.module.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public class LazyHotelsResult {

    public List<Hotel> hotels;
    public String latitude;
    public String longitude;
    public String review_score;

    public static class Hotel {
        public Location location;
        public String hotel_id;

    }

    public static class Location {
        public double latitude;
        public double longitude;

        public LatLng getLatLng() {
            return new LatLng(latitude, longitude);
        }
    }
}
