package lazy.hackthon.com.lazy.map;

import java.util.List;

import lazy.hackthon.com.module.data.HotelInfo;
import lazy.hackthon.com.module.data.LazyHotelsResult;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public interface MapContract {
    interface Actions{
        void loadHotels(String checkin, String checkout, String place_ids[]);
        void loadHotelsInfo(String checkin, String checkout, String hotel_ids[]);
        void loadPhoto(String hotel_id);
    }
    interface Views{
        void showHotels(List<LazyHotelsResult.Hotel> hotelList);
        void updateHotelInfo(List<HotelInfo> hotel);
        void updatePhoto(String hotelId, String imgUrl);
    }
}
