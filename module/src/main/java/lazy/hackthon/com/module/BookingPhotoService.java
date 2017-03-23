package lazy.hackthon.com.module;

import java.util.List;

import lazy.hackthon.com.module.data.PhotoResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public interface BookingPhotoService {
    //https://distribution-xml.booking.com/json/bookings.getHotelDescriptionPhotos?
    @GET("bookings.getHotelDescriptionPhotos")
    Observable<List<PhotoResult>> getPhotoResult(@Query("hotel_id")String hotel_id);
}
