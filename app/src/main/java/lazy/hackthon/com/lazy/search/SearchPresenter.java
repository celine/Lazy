package lazy.hackthon.com.lazy.search;


import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import lazy.hackthon.com.module.data.PredictionList;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class SearchPresenter implements SearchContract.Actions {
    SearchRepository searchRepository;
    SearchContract.Views mViews;
    TextWatcher textWatcher;

    public SearchPresenter(SearchRepository repository, SearchContract.Views views) {
        searchRepository = repository;
        mViews = views;
    }

    @Override
    public void loadPlaces(final String key) {
        Timber.d("load places " + key);
        searchRepository.getSimilarPlaces(key).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<PredictionList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e("error", e);
            }

            @Override
            public void onNext(PredictionList predictions) {
                List<PredictionList.Prediction> predictionList = predictions.predictions;
                Timber.d("predictionList " + predictionList.size());
                mViews.showVenues(predictionList);
            }
        });
    }

    private Subscription textChangeSubscription;

    public void listenTextChange(final EditText searchText) {
        textChangeSubscription = RxAndroidTextObservable.addTextChangeObservable(searchText).debounce(3000,TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread()).concatMap(new Func1<String, Observable<PredictionList>>() {
            @Override
            public Observable<PredictionList> call(String key) {
                return searchRepository.getSimilarPlaces(key);
            }
        }).map(new Func1<PredictionList, List<PredictionList.Prediction>>() {
            @Override
            public List<PredictionList.Prediction> call(PredictionList predictionList) {
                List<PredictionList.Prediction> predictions = predictionList.predictions;
                Timber.d("prediction size " + predictions.size());
                return predictions;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<List<PredictionList.Prediction>>() {
            @Override
            public void onCompleted() {
                Timber.d("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e,"error");
            }

            @Override
            public void onNext(List<PredictionList.Prediction> predictions) {
                mViews.updateSearchKey(searchText.getText().toString());
                mViews.showVenues(predictions);
            }
        });
    }

    public void cancelSubscription() {
        textChangeSubscription.unsubscribe();
        mViews.removeTextWatcher(textWatcher);
    }
}
