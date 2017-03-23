package lazy.hackthon.com.lazy.selectvenue;

import java.util.List;

import lazy.hackthon.com.module.data.PlaceResults;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public interface SelectVenueContract {
    static interface Actions{
        void loadVenues(String search);

    }
    static interface Views{
        void updateViews(List<PlaceResults.Place>venueDatas);
    }
}
