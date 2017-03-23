package lazy.hackthon.com.module;

import java.util.List;

import lazy.hackthon.com.module.data.HotelResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public interface BookingService {
    @GET("bookings.getBlockAvailability")
    Observable<List<HotelResult>> getHotelInfo(@Query("arrival_date") String arrival_date, @Query("departure_date") String departure_date, @Query("hotel_ids") String hotel_ids);

}
