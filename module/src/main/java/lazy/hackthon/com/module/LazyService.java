package lazy.hackthon.com.module;

import lazy.hackthon.com.module.data.LazyHotelsResult;
import lazy.hackthon.com.module.data.PlaceResults;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public interface LazyService {

    @GET("attractions/")
    Observable<PlaceResults> getPlaceResults(@Query("place_id") String place_id);

    //checkin=2017-06-08
    @GET("attractions/hotels")
    Observable<LazyHotelsResult> getHotels(@Query("checkin") String checkin, @Query("checkout") String checkOut, @Query("place_ids") String place_ids);

}
