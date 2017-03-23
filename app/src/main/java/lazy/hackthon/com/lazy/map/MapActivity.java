package lazy.hackthon.com.lazy.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import lazy.hackthon.com.lazy.MyApplication;
import lazy.hackthon.com.lazy.R;
import lazy.hackthon.com.module.data.HotelInfo;
import lazy.hackthon.com.module.data.LazyHotelsResult;
import lazy.hackthon.com.module.data.PlaceResults;
import timber.log.Timber;

import static lazy.hackthon.com.module.data.Constants.CENTER;
import static lazy.hackthon.com.module.data.Constants.EXTRA_PLACES;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, MapContract.Views {
    private GoogleMap mMap;
    Map<String, Object> markerMap = new HashMap<>();
    List<Marker> hotelsMarkers = new ArrayList<>();
    View filterView;
    View popupView;
    @Inject
    MapRepository mapRepository;
    MapPresenter mapPresenter;
    Map<String, HotelInfo> hotelInfoMap;
    View filterMenu;
    RangeSeekBar rangeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).component().inject(this);
        mapPresenter = new MapPresenter(mapRepository, this);

        setContentView(R.layout.activity_maps);
        filterMenu = findViewById(R.id.filterMenu);
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeSeekBar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        popupView = findViewById(R.id.popupView);
        filterView = findViewById(R.id.filterView);
        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMenu.setVisibility(View.VISIBLE);
            }
        });
        View closeView = popupView.findViewById(R.id.close);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.setVisibility(View.GONE);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);
        Intent intent = getIntent();
        ArrayList<PlaceResults.Place> placeArrayList = intent.getParcelableArrayListExtra(EXTRA_PLACES);
        boolean first = true;
        Timber.d("decode");

        for (PlaceResults.Place place : placeArrayList) {
            LatLng latlng = place.getLatlng();
            Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title(place.name));
            Timber.d("latlng " + latlng.toString());

            markerMap.put(marker.getId(), place);
            if (first) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            }

        }
        String placeIds[] = new String[placeArrayList.size()];
        for (int i = 0; i < placeArrayList.size(); i++) {
            placeIds[i] = placeArrayList.get(i).place_id;
        }
        mapPresenter.loadHotels("2017-06-11", "2017-07-01", placeIds);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Object object = markerMap.get(marker.getId());
                if (object instanceof PlaceResults.Place) {
                    PlaceResults.Place place = (PlaceResults.Place) object;
                    showPopupWindow(place);
                } else if (object instanceof LazyHotelsResult.Hotel) {
                    LazyHotelsResult.Hotel hotel = (LazyHotelsResult.Hotel) object;
                    showPopupWindow(hotel);
                }
                return false;
            }
        });
    }

    public void showPopupWindow(PlaceResults.Place place) {
        popupView.setVisibility(View.VISIBLE);
        filterMenu.setVisibility(View.GONE);
        popupView.findViewById(R.id.book).setVisibility(View.GONE);
        ImageView imageView = (ImageView) popupView.findViewById(R.id.img);
        Picasso.with(getBaseContext()).load(place.getPhoto()).into(imageView);
        TextView textView = (TextView) popupView.findViewById(R.id.name);
        textView.setText(place.name);

    }

    public void showPopupWindow(LazyHotelsResult.Hotel hotel) {
        popupView.setVisibility(View.VISIBLE);
        filterMenu.setVisibility(View.GONE);
        View bookingView = popupView.findViewById(R.id.book);

        synchronized (mutex) {
            HotelInfo hotelInfo = hotelInfoMap.get(hotel.hotel_id);
            if (hotelInfo != null) {
                TextView textView = (TextView) popupView.findViewById(R.id.name);
                textView.setText(hotelInfo.name);
            }
        }
        bookingView.setVisibility(View.VISIBLE);
        bookingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setData(Uri.parse("http://booking.com"));
                startActivity(intent);
            }
        });

    }

    @Override
    public void showHotels(List<LazyHotelsResult.Hotel> hotelList) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hotel);
        Bitmap result = Bitmap.createScaledBitmap(bitmap,
                80, 80, false);
        Location centerLoc = null;
        hotelInfoMap = new HashMap<>();
        double radius = 0;
        BitmapDescriptor hotelIcon = BitmapDescriptorFactory.fromBitmap(result);
        List<String> hotelIds = new ArrayList<>();

        for (LazyHotelsResult.Hotel hotel : hotelList) {
            Timber.d("hotel  " + hotel.hotel_id);
            LatLng latLng = hotel.location.getLatLng();
            Location location = new Location("loc");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (hotel.hotel_id.equals(CENTER)) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotel.location.getLatLng(), 14));
                centerLoc = location;
                //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                //mMap.zoomI
            } else {

                hotelIds.add(hotel.hotel_id);
                LatLng latlng = hotel.location.getLatLng();
                Timber.d("latlng " + latlng.toString());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).title("hotel").icon(hotelIcon));
                hotelsMarkers.add(marker);
                // builder.include(latLng);
                markerMap.put(marker.getId(), hotel);
                if (centerLoc != null) {
                    radius = Math.max(radius, centerLoc.distanceTo(location));
                }
            }
            //   LatLngBounds bounds = builder.build();
            //   int padding = ((20 * 10) / 100); // offset from edges of the map
            // in pixels
            //  CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
            //         padding);
            // mMap.animateCamera(cu);

        }
        CircleOptions circleOptions = new CircleOptions().center(
                new LatLng(centerLoc.getLatitude(), centerLoc.getLongitude())).radius(radius + 2).
                fillColor(Color.TRANSPARENT).strokeColor(Color.BLUE).strokeWidth(5);
        mMap.addCircle(circleOptions);

        Timber.d("load hotels info");
        mapPresenter.loadHotelsInfo("2017-06-11", "2017-06-12", hotelIds.toArray(new String[0]));
    }

    Object mutex = new Object();

    @Override
    public void updateHotelInfo(List<HotelInfo> hotelList) {
        float minValue = Integer.MAX_VALUE;
        float maxValue = 0;
        Timber.d("update hotel info");
        synchronized (mutex) {
            for (HotelInfo hotelInfo : hotelList) {
                minValue = Math.min(minValue, hotelInfo.minPrice);
                maxValue = Math.max(maxValue, hotelInfo.maxPrice);
                Timber.d("hotel id " + hotelInfo.hotel_id);
                hotelInfoMap.put(hotelInfo.hotel_id, hotelInfo);
            }
            rangeSeekBar.setRangeValues(minValue, maxValue);
            rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Float>() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Float minValue, Float maxValue) {
                    Timber.d("select " + minValue + " " + maxValue);
                    for (Marker marker : hotelsMarkers) {
                        LazyHotelsResult.Hotel hotel = (LazyHotelsResult.Hotel) markerMap.get(marker.getId());
                        HotelInfo hotelInfo = hotelInfoMap.get(hotel.hotel_id);
                        Timber.d("get hotel id " + hotel.hotel_id);
                        if (hotelInfo == null) {
                            marker.setVisible(false);
                        } else {
                            Timber.d("minValue " + hotelInfo.minPrice);
                            Timber.d("maxValue " + hotelInfo.maxPrice);
                            if (hotelInfo.minPrice < minValue && hotelInfo.maxPrice > maxValue) {
                                marker.setVisible(false);
                            } else {
                                marker.setVisible(true);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void updatePhoto(String hotelId, String imgUrl) {

    }


}
