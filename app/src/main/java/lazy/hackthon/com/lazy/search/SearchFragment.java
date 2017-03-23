package lazy.hackthon.com.lazy.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import lazy.hackthon.com.lazy.MyApplication;
import lazy.hackthon.com.lazy.R;
import lazy.hackthon.com.lazy.selectvenue.SelectVenueFragment;
import lazy.hackthon.com.module.data.PredictionList;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment implements SearchContract.Views, OnItemClickListener {
    SearchPresenter searchPresenter;
    EditText searchText;
    Button nextButton;
    RecyclerView recyclerView;
    @Inject
    SearchRepository searchRepository;
    PredictionAdapter mAdapter;

    public SearchFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mAdapter!= null && mAdapter.getItemCount() > 0){
            for(int i =0; i < mAdapter.getItemCount();i++){

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchText = (EditText) view.findViewById(R.id.search);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        ImageView backgroundImage = (ImageView) view.findViewById(R.id.background_image_view);
        Picasso.with(getContext()).load("http://www.taipei-101.com.tw/upload/news/201410/20141016165159LKHDPKFO.jpg").into(backgroundImage);
     /*   searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PredictionList.Prediction prediction = mAdapter.getItem(position);
                Timber.d("place_id " + prediction.place_id);
                searchText.dismissDropDown();
                SelectVenueFragment selectVenueFragment = SelectVenueFragment.newInstance(prediction.place_id);
                getFragmentManager().beginTransaction().replace(R.id.fragment, selectVenueFragment).commit();

            }
        });*/
        Log.d("WTEST", "add Text change");

        mAdapter = new PredictionAdapter(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchPresenter.cancelSubscription();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).component().inject(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);
        searchPresenter = new SearchPresenter(searchRepository, this);

        searchPresenter.listenTextChange(searchText);
        // getActivity().getSu().setTitle(R.string.search_location);
    }

    @Override
    public void showVenues(List<PredictionList.Prediction> predictionList) {
        mAdapter.updatePredictions(predictionList);
        Timber.d("update predictions " + predictionList.size());
        // searchText.showDropDown();
    }

    @Override
    public void removeTextWatcher(TextWatcher textWatcher) {
        searchText.removeTextChangedListener(textWatcher);
    }

    @Override
    public void updateSearchKey(String key) {
        mAdapter.updateFilterKey(key);
    }

    InputMethodManager inputMethodManager;
    @Override
    public void onSelectPrediction(PredictionList.Prediction prediction) {
        Timber.d("place_id " + prediction.place_id);
        SelectVenueFragment selectVenueFragment = SelectVenueFragment.newInstance(prediction.place_id);
        getFragmentManager().beginTransaction().replace(R.id.fragment, selectVenueFragment).commit();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
