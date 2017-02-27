package ch.christofbuechi.delaywarner;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ch.christofbuechi.delaywarner.base.BasePresenter;
import ch.christofbuechi.delaywarner.network.TransportService;
import ch.christofbuechi.delaywarner.network.model.PassList;
import ch.christofbuechi.delaywarner.network.model.Station;
import ch.christofbuechi.delaywarner.network.model.StationBoard;
import ch.christofbuechi.delaywarner.network.model.StationBoardWrapper;
import ch.christofbuechi.delaywarner.util.GpsUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */
public class CheckPresenter extends BasePresenter<CheckUI> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    @Inject
    TransportService transportService;
    private String mLastUpdateTime;
    private Location lastLocation;


    @Inject
    public CheckPresenter() {
    }


    public void callService(Location location) {

//        transportService.getStations("47.3768866", "8.541694000000007").subscribe(new Consumer<StationWrapper>() {
        transportService.getStations(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())).subscribe(stationWrapperResponse -> {
            // TODO: 07.01.2017 handle response
            List<Station> stations = stationWrapperResponse.getStations();
            Timber.d("List of Statins: " + stations.size());
//            getUi().showStationList(stations);
            if (stations.size() > 0) {
                Observable<StationBoardWrapper> stationTable = transportService.getTimeTable(stations.get(0));
                stationTable.subscribe(stationBoardWrapper -> {
                    int delayInseconds = getDelay(stationBoardWrapper.getStationboard().get(0));
                    getUi().showDelayForStation(stationBoardWrapper.getStation(), delayInseconds);
                }, throwable -> {
                    Timber.e("Error occured");
                    Timber.e(throwable);
                });
            }
        }, throwable -> {
            Timber.d(throwable);
            // TODO: 07.01.2017 error happened
        });
        // TODO call server...
    }

    private int getDelay(StationBoard stationboard) {
        List<PassList> list = stationboard.getPassList();

        int rememberStations = 10;
        if (list.size() < 10) {
            rememberStations = list.size();
        }


        for (int i = 0; i < rememberStations; i++) {
            PassList passlist = list.get(i);
            if (passlist.getDelay() == null) {
                return 0;
            } else {
                if (list.get(i).getDelay() instanceof Integer) {
                    return (Integer) list.get(i).getDelay();
                }
            }

        }
        return 0;
    }


    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ?
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        getUi().showErrorcase();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (lastLocation == null) {
            updateLocation(location);
        } else {
            if (GpsUtil.distance(location, lastLocation) > 0.1) {
                updateLocation(location);
            }
        }


    }

    private void updateLocation(Location location) {
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        lastLocation = location;
        getUi().updateUI(location, mLastUpdateTime);
        callService(location);
    }

}
