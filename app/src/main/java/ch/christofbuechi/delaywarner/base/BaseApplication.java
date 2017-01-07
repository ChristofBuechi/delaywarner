package ch.christofbuechi.delaywarner.base;

import android.app.Application;

import com.facebook.stetho.Stetho;

import ch.christofbuechi.delaywarner.BuildConfig;
import ch.christofbuechi.delaywarner.base.dagger.AppComponent;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Stetho.initializeWithDefaults(this);
        AppComponent.Holder.initialize(this);
    }

}
