package lazy.hackthon.com.lazy.selectvenue;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;
import lazy.hackthon.com.lazy.selectvenue.SelectVenueContract;
import lazy.hackthon.com.module.APIClient;
import lazy.hackthon.com.module.data.PlaceResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class VenuesRepository {
    APIClient apiClient;

    public VenuesRepository(APIClient apiClient) {
        this.apiClient = apiClient;
    }

    public Observable<List<PlaceResults.Place>> getSelectedVenues(final String place_id) {
        return apiClient.getLazyService().getPlaceResults(place_id).concatMap(new Func1<PlaceResults, Observable<? extends List<PlaceResults.Place>>>() {
            @Override
            public Observable<? extends List<PlaceResults.Place>> call(PlaceResults placeResults) {
                List<PlaceResults.Place>places = new ArrayList<PlaceResults.Place>();
                for(int i = 0;i < placeResults.Results.size();i++){
                    PlaceResults.Place place = placeResults.Results.get(i);
                    if(place.getPhoto()!= null){
                        places.add(place);
                    }
                }
                return Observable.just(places);
            }
        });
    }
}
