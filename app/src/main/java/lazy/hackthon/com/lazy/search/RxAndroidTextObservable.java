package lazy.hackthon.com.lazy.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by wenchihhsieh on 2017/3/16.
 */

public class RxAndroidTextObservable {
    public static Observable<String>addTextChangeObservable(final TextView textView){

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        subscriber.onNext(s.toString());
                    }
                };
                textView.addTextChangedListener(textWatcher);
            }
        });
    }
}
