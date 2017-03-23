package lazy.hackthon.com.lazy.map;

import java.util.List;

import lazy.hackthon.com.module.data.HotelInfo;
import lazy.hackthon.com.module.data.LazyHotelsResult;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/12.
 */

public class MapPresenter implements MapContract.Actions {
    MapRepository mapRepository;
    MapContract.Views mViews;

    public MapPresenter(MapRepository repository, MapContract.Views views) {
        mapRepository = repository;
        mViews = views;
    }

    @Override
    public void loadHotels(String checkin, String checkout, String[] place_ids) {
        mapRepository.getHotelResults(checkin, checkout, place_ids).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<List<LazyHotelsResult.Hotel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e("loadHotels error ?", e);

            }

            @Override
            public void onNext(List<LazyHotelsResult.Hotel> hotels) {
                mViews.showHotels(hotels);
            }
        });
    }

    @Override
    public void loadHotelsInfo(String checkin, String checkout, String[] hotel_ids) {
        mapRepository.getHotelInfos(checkin, checkout, hotel_ids).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<List<HotelInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e("loadHotelsInfo error:", e);
            }

            @Override
            public void onNext(List<HotelInfo> hotelInfos) {
                mViews.updateHotelInfo(hotelInfos);
            }
        });
    }

    @Override
    public void loadPhoto(String hotel_id) {
        mapRepository.loadPhoto(hotel_id);
    }


}
