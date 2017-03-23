package lazy.hackthon.com.lazy.map;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lazy.hackthon.com.module.APIClient;
import lazy.hackthon.com.module.data.Constants;
import lazy.hackthon.com.module.data.HotelInfo;
import lazy.hackthon.com.module.data.HotelResult;
import lazy.hackthon.com.module.data.LazyHotelsResult;
import lazy.hackthon.com.module.data.PhotoResult;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public class MapRepository {
    APIClient apiClient;

    public MapRepository(APIClient apiClient) {
        this.apiClient = apiClient;
    }

    public Observable<List<LazyHotelsResult.Hotel>> getHotelResults(String checkIn, String checkOut, String ids[]) {
        return apiClient.getLazyService().getHotels(checkIn, checkOut, TextUtils.join(",", ids)).concatMap(new Func1<LazyHotelsResult, Observable<? extends List<LazyHotelsResult.Hotel>>>() {
            @Override
            public Observable<? extends List<LazyHotelsResult.Hotel>> call(LazyHotelsResult hotelsResult) {
                LazyHotelsResult.Hotel centerHotel = new LazyHotelsResult.Hotel();
                centerHotel.hotel_id = Constants.CENTER;
                Timber.d("add hotel " + hotelsResult.hotels.size());
                centerHotel.location = new LazyHotelsResult.Location();
                try {
                    centerHotel.location.latitude = Double.parseDouble(hotelsResult.latitude);
                    centerHotel.location.longitude = Double.parseDouble(hotelsResult.longitude);
                } catch (NumberFormatException e) {
                    Timber.e("error parse ", e);
                }
                List<LazyHotelsResult.Hotel> hotels = new ArrayList<>();
                hotels.add(centerHotel);
                hotels.addAll(hotelsResult.hotels);

                return Observable.just(hotels);

            }
        });
    }

    public Observable<List<HotelInfo>> getHotelInfos(String checkin, String checkout, String[] hotel_ids) {
        return apiClient.getBookingService().getHotelInfo(checkin, checkout, TextUtils.join(",", hotel_ids)).
                concatMap(new Func1<List<HotelResult>, Observable<? extends List<HotelInfo>>>() {
                    @Override
                    public Observable<? extends List<HotelInfo>> call(List<HotelResult> hotelResults) {
                        List<HotelInfo> hotelInfos = new ArrayList<HotelInfo>();
                        float minPrice = Integer.MAX_VALUE;
                        float maxPrice = 0;
                        for (HotelResult result : hotelResults) {
                            HotelInfo info = new HotelInfo();
                            info.hotel_id = result.hotel_id;
                            for (HotelResult.Block block : result.block) {
                                if (block.incremental_price != null && block.incremental_price.size() > 0) {
                                    float tmpMin = Float.parseFloat(block.incremental_price.get(0).price);
                                    int allSize = block.incremental_price.size();
                                    float tmpMax = Float.parseFloat(block.incremental_price.get(allSize - 1).price);
                                    minPrice = Math.min(tmpMin, minPrice);
                                    maxPrice = Math.max(tmpMax, maxPrice);
                                }
                            }
                            info.minPrice = minPrice;
                            info.maxPrice = maxPrice;
                            hotelInfos.add(info);
                        }
                        return Observable.just(hotelInfos);
                    }
                });

    }

    public Observable<PhotoResult> loadPhoto(String hotel_id) {
        return null;
    }
}
