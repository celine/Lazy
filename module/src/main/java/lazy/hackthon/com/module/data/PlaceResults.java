package lazy.hackthon.com.module.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lazy.hackthon.com.module.util.PhotoUtil;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class PlaceResults {
    public List<Place> Results;

    public static class Place implements Parcelable {
        public String name;
        public String place_id;
        public String photoUrl;
        public List<Photo> photos;
        LatLng latlng;
        public Geometry geometry;

        protected Place(Parcel in) {
            name = in.readString();
            place_id = in.readString();
            photoUrl = in.readString();
            latlng = in.readParcelable(LatLng.class.getClassLoader());
        }

        public static final Creator<Place> CREATOR = new Creator<Place>() {
            @Override
            public Place createFromParcel(Parcel in) {
                return new Place(in);
            }

            @Override
            public Place[] newArray(int size) {
                return new Place[size];
            }
        };

        public String getPhoto() {
            if (photoUrl == null) {
                if (photos != null && photos.size() > 0) {
                    photoUrl = PhotoUtil.getLargePhotoUrl(photos.get(0).photo_reference, Constants.GOOGLE_API_KEY);
                }
            }
            return photoUrl;
        }

        public LatLng getLatlng() {
            if(latlng == null){
                Loc loc = geometry.location;
                latlng = new LatLng(Double.parseDouble(loc.lat), Double.parseDouble(loc.lng));
            }
            return latlng;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(place_id);
            dest.writeString(getPhoto());
            dest.writeParcelable(getLatlng(), flags);
        }
    }

    public static class Geometry {
        Loc location;
    }

    public static class Loc {
        String lat;
        String lng;
    }
}
