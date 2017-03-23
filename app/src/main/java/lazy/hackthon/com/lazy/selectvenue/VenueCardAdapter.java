package lazy.hackthon.com.lazy.selectvenue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.BaseCardStackAdapter;
import com.andtinder.view.CardStackAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import lazy.hackthon.com.lazy.R;
import lazy.hackthon.com.module.data.PlaceResults;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class VenueCardAdapter extends BaseAdapter {
    Context mContext;
    List<PlaceResults.Place> venueDataList;

    public VenueCardAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return venueDataList == null ? 0 : venueDataList.size();
    }

    @Override
    public PlaceResults.Place getItem(int i) {
        return venueDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.vnue_view, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PlaceResults.Place venueData = getItem(position);
        holder.textView.setText(venueData.name);
        // Picasso.with(mContext).cancelRequest(holder.imageView);
        int imgSize = mContext.getResources().getDimensionPixelOffset(R.dimen.imageSize);
        Picasso.with(mContext).load(venueData.getPhoto()).resize(imgSize, imgSize).centerCrop().into(holder.imageView);
        return view;
    }

    public void updateData(List<PlaceResults.Place> venueData) {
        venueDataList = venueData;
        notifyDataSetChanged();
    }

    public void removeData(Object dataObject) {
        venueDataList.remove(dataObject);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.img);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
