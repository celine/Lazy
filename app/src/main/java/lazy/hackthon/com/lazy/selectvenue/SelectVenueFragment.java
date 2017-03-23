package lazy.hackthon.com.lazy.selectvenue;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lazy.hackthon.com.lazy.MyApplication;
import lazy.hackthon.com.lazy.R;
import lazy.hackthon.com.lazy.map.MapActivity;
import lazy.hackthon.com.module.data.PlaceResults;
import timber.log.Timber;

import static lazy.hackthon.com.module.data.Constants.EXTRA_PLACES;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class SelectVenueFragment extends Fragment implements SelectVenueContract.Views {
    SwipeFlingAdapterView swipeFlingAdapterView;
    @Inject
    VenuesRepository venuesRepository;
    VenueCardAdapter mAdapter;
    SelectVenuesPresenter selectVenuesPresenter;
    boolean removeStart;
    private static final String EXTRA_SEARCH_KEY = "EXTRA_SEARCH_KEY";

    public static SelectVenueFragment newInstance(String search) {
        Bundle args = new Bundle();
        args.putString(EXTRA_SEARCH_KEY, search);
        SelectVenueFragment fragment = new SelectVenueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<PlaceResults.Place> selectedPlaces = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_venues, container, false);
        swipeFlingAdapterView = (SwipeFlingAdapterView) rootView.findViewById(R.id.frame);
        final View emptyView = rootView.findViewById(R.id.emptyView);

        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Timber.d("first remove ");

                removeStart = true;
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Timber.d("left remove " + dataObject);
                mAdapter.removeData(dataObject);
                if(mAdapter.getCount() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Timber.d("right remove " + dataObject);
                selectedPlaces.add((PlaceResults.Place) dataObject);
                mAdapter.removeData(dataObject);
                if(mAdapter.getCount() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Timber.d("itemsInAdapter " + itemsInAdapter);
                if (itemsInAdapter == 0 && removeStart) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        });
        Button next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(EXTRA_PLACES, selectedPlaces);
                intent.setClass(getActivity(), MapActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).component().inject(this);
        mAdapter = new VenueCardAdapter(getActivity());
        swipeFlingAdapterView.setAdapter(mAdapter);
        selectVenuesPresenter = new SelectVenuesPresenter(venuesRepository, this);
        String searchKey = getArguments().getString(EXTRA_SEARCH_KEY);
        selectVenuesPresenter.loadVenues(searchKey);
        //adapter.add();
    }

    @Override
    public void updateViews(List<PlaceResults.Place> venueDatas) {
        mAdapter.updateData(venueDatas);
    }
}
