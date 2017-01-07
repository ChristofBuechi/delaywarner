package ch.christofbuechi.delaywarner.base.dagger;

import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import ch.christofbuechi.delaywarner.BuildConfig;
import ch.christofbuechi.delaywarner.network.SchedulerWrapper;
import ch.christofbuechi.delaywarner.network.TransportService;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Christof on 07.01.2017.
 */

@Module
public class ApplicationModule {
    private Context applicationContext;

    public ApplicationModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    public Context provideContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    @NonNull
    public Gson provideGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss ZZZ").create();
    }

    @Provides
    @Singleton
    @NonNull
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }


    @Provides
    @Singleton
    @NonNull
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl("http://transport.opendata.ch/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public SchedulerWrapper providesSchedulerWrapper() {
        return new SchedulerWrapper();
    }


    @Provides
    @Singleton
    public TransportService provideTravelDaysService(Retrofit retrofit, SchedulerWrapper schedulerWrapper) {
        return createTravelDayService(retrofit, schedulerWrapper);
    }

    protected TransportService createTravelDayService(final Retrofit retrofit, final SchedulerWrapper schedulerWrapper) {
        return new TransportService(retrofit, schedulerWrapper);
    }
}
