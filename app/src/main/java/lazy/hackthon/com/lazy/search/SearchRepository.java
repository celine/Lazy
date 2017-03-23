package lazy.hackthon.com.lazy.search;

import java.util.List;

import lazy.hackthon.com.module.APIClient;
import lazy.hackthon.com.module.PlacesService;
import lazy.hackthon.com.module.data.Constants;
import lazy.hackthon.com.module.data.HotelResult;
import lazy.hackthon.com.module.data.PredictionList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class SearchRepository implements Constants {
    APIClient apiClient;

    public SearchRepository(APIClient client) {
        apiClient = client;
    }

    public Observable<PredictionList> getSimilarPlaces(String key) {
        PlacesService placeServices = apiClient.getPlacesService();
        Timber.d("Sending request " + key);

        return placeServices.getPredictionList(key);
    }
}
