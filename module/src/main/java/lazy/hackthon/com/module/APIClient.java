package lazy.hackthon.com.module;

import java.io.IOException;

import lazy.hackthon.com.module.data.Constants;
import lazy.hackthon.com.module.data.PredictionList;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static lazy.hackthon.com.module.data.Constants.GOOGLE_API_KEY;
import static lazy.hackthon.com.module.data.Constants.USER_NAME;
import static lazy.hackthon.com.module.data.Constants.PASSWORD;


/**
 * Created by wenchihhsieh on 2017/3/11.
 */

public class APIClient {
    public PlacesService getPlacesService() {
        return placesService;
    }

    PlacesService placesService;

    public LazyService getLazyService() {
        return lazyService;
    }

    LazyService lazyService;

    public BookingService getBookingService() {
        return bookingService;
    }

    BookingService bookingService;
    BookingPhotoService bookingPhotoService;

    public BookingPhotoService getBookingPhotoService() {
        return bookingPhotoService;
    }

    public APIClient() {
        Timber.d("Get APIClient");
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient placeClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Timber.d("request");
                Request request = chain.request();
                HttpUrl httpUrl = request.url().newBuilder().addQueryParameter("key", GOOGLE_API_KEY).build();
                request = request.newBuilder().url(httpUrl).build();
                return chain.proceed(request);
            }
        }).addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/place/").client(placeClient)
                .build();
        placesService = retrofit.create(PlacesService.class);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient loggingClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit lazyRetrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).client(loggingClient)
                .baseUrl("http://172.20.10.8:5566/")
                .build();
        lazyService = lazyRetrofit.create(LazyService.class);

        String authToken = Credentials.basic(USER_NAME, PASSWORD);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new AuthenticationInterceptor(authToken)).build();

        Retrofit bookingRetrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://distribution-xml.booking.com/json/").client(client)
                .build();
        bookingService = bookingRetrofit.create(BookingService.class);
        //https://distribution-xml.booking.com/json/
        Retrofit bookingPhotoRetrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://distribution-xml.booking.com/json/")
                .build();
        bookingPhotoService = bookingPhotoRetrofit.create(BookingPhotoService.class);

    }
}
