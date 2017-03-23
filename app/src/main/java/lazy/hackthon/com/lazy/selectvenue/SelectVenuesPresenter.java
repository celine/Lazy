package lazy.hackthon.com.lazy.selectvenue;

import java.util.List;

import lazy.hackthon.com.module.data.PlaceResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class SelectVenuesPresenter implements SelectVenueContract.Actions {
    VenuesRepository venuesRepository;
    SelectVenueContract.Views mViews;

    public SelectVenuesPresenter(VenuesRepository repository, SelectVenueContract.Views views) {
        venuesRepository = repository;
        mViews = views;
    }

    @Override
    public void loadVenues(String search) {
        venuesRepository.getSelectedVenues(search).subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<PlaceResults.Place>>() {
            @Override
            public void onCompleted() {
                Timber.d("complete");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("error",e);
            }

            @Override
            public void onNext(List<PlaceResults.Place> venueDatas) {
                Timber.d("venue " + venueDatas.size());
                mViews.updateViews(venueDatas);
            }
        });
    }
}
