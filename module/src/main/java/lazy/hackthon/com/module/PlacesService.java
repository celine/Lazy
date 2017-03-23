package lazy.hackthon.com.module;

import lazy.hackthon.com.module.data.PredictionList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public interface PlacesService {
    @GET("autocomplete/json?types=geocode")
    Observable<PredictionList> getPredictionList(@Query("input") String input);
}
