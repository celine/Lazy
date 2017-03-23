package lazy.hackthon.com.lazy.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lazy.hackthon.com.lazy.R;
import lazy.hackthon.com.lazy.selectvenue.VenueCardAdapter;
import lazy.hackthon.com.module.data.PlaceResults;
import lazy.hackthon.com.module.data.PredictionList;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */
import lazy.hackthon.com.module.data.PredictionList.Prediction;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder> {
    Context mContext;
    List<Prediction> allPredictions;
    OnItemClickListener itemClickListener;

    public PredictionAdapter(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void updateFilterKey(String filterKey) {
        filterData();
    }

    void filterData() {
        notifyDataSetChanged();
    }

    void updatePredictions(List<Prediction> predictions) {
        allPredictions = predictions;
        filterData();
    }

    @Override
    public PredictionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text, parent, false);
        return new PredictionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PredictionViewHolder holder, final int position) {
        Prediction prediction = allPredictions.get(position);
        holder.textView.setText(prediction.description);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prediction prediction = allPredictions.get(position);
                itemClickListener.onSelectPrediction(prediction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allPredictions == null ? 0 : allPredictions.size();
    }

    public static class PredictionViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public PredictionViewHolder(View view) {
            super(view);
            textView = (TextView) view;
        }

    }



}
