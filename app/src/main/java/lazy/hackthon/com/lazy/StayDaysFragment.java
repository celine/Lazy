package lazy.hackthon.com.lazy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class StayDaysFragment extends Fragment {

    public StayDaysFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle(R.string.search_location);
        return inflater.inflate(R.layout.fragment_stay, container, false);
    }
}
