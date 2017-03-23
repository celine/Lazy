package lazy.hackthon.com.module.data;

import java.util.List;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class PredictionList {
    public List<Prediction> predictions;

    public static class Prediction {
        public String place_id;
        public String description;

        @Override
        public String toString() {
            return description;
        }
    }
}
