package lazy.hackthon.com.lazy;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import lazy.hackthon.com.lazy.map.MapActivity;
import lazy.hackthon.com.lazy.search.SearchFragment;
import lazy.hackthon.com.lazy.selectvenue.SelectVenueFragment;
import timber.log.Timber;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class MyApplication extends Application {
    @Singleton
    @Component(modules = AppModule.class)
    public interface ApplicationComponent {
        void inject(MainActivity homeActivity);
        void inject(MapActivity mapActivity);
        void inject(SelectVenueFragment selectVenueFragment);
        void inject(SearchFragment searchFragment);
    }

    @Override public void onCreate() {
        super.onCreate();
        component = DaggerMyApplication_ApplicationComponent.builder().appModule(new AppModule(this))
                .build();
        Timber.plant(new Timber.DebugTree());
    }
    private ApplicationComponent component;
    public ApplicationComponent component() {
        return component;
    }
}
