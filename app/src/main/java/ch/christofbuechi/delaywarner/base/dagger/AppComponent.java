package ch.christofbuechi.delaywarner.base.dagger;

import java.util.logging.Logger;

import javax.inject.Singleton;

import ch.christofbuechi.delaywarner.base.BaseApplication;
import dagger.Component;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
})

public interface AppComponent extends InjectionComponent {


    enum Holder {

        ;

        private static AppComponent appComponent;

        public static AppComponent getAppComponent() {
            return appComponent;
        }

        public static synchronized void initialize(BaseApplication application) {
            if (appComponent == null) {
                Timber.d(AppComponent.class.getSimpleName(), "AppComponent is null");
                appComponent = createAppComponent(application);
            }
        }

        private static AppComponent createAppComponent(BaseApplication application) {
            Timber.d(AppComponent.class.getSimpleName(), "createAppComponent");

            return DaggerAppComponent
                    .builder()
                    .applicationModule(new ApplicationModule(application.getApplicationContext()))
                    .build();

        }

        public static void setAppComponent(AppComponent component) {
            appComponent = component;
        }
    }
}