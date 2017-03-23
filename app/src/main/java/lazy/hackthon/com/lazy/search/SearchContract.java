package lazy.hackthon.com.lazy.search;

import android.text.TextWatcher;

import java.util.List;

import lazy.hackthon.com.module.data.PredictionList;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public interface SearchContract {
    interface Actions {
        void loadPlaces(String key);
    }

    interface Views {
        void showVenues(List<PredictionList.Prediction> predictionList);

        void removeTextWatcher(TextWatcher textWatcher);

        void updateSearchKey(String key);
    }
}
