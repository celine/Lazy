package lazy.hackthon.com.lazy;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lazy.hackthon.com.lazy.map.MapRepository;
import lazy.hackthon.com.lazy.search.SearchRepository;
import lazy.hackthon.com.lazy.selectvenue.VenuesRepository;
import lazy.hackthon.com.module.APIClient;

/**
 * Created by wenchihhsieh on 2017/3/11.
 */
@Module
public class AppModule {
    MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    APIClient provideAPIClient() {
        return new APIClient();
    }

    @Provides
    @Singleton
    VenuesRepository provideVenueRepository(APIClient apiClient) {
        return new VenuesRepository(apiClient);
    }
    @Provides
    @Singleton
    SearchRepository provideSearchRepository(APIClient apiClient) {
        return new SearchRepository(apiClient);
    }

    @Provides
    @Singleton
    MapRepository provideMapRepository(APIClient apiClient) {
        return new MapRepository(apiClient);
    }
}
