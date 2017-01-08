package ch.christofbuechi.delaywarner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import ch.christofbuechi.delaywarner.base.BasePresenter;
import ch.christofbuechi.delaywarner.network.TransportService;
import ch.christofbuechi.delaywarner.util.PlayServiceChecker;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */
public class CheckPresenter extends BasePresenter<CheckUI> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_LOCATION = 11;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;
    @Inject
    TransportService transportService;

    private GoogleApiClient mGoogleApiClient;


    @Inject
    public CheckPresenter() {
    }

    public void onButtonPressed() {
        if (PermissionUtil.with(getUi().getFragment()).has(ACCESS_COARSE_LOCATION)) {
            requestLocation();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        PermissionUtil.with(getUi().getFragment()).request(ACCESS_COARSE_LOCATION).onAllGranted(
                new Func() {
                    @Override
                    protected void call() {
                        requestLocation();
                    }
                }).onAnyDenied(
                new Func() {
                    @Override
                    protected void call() {
                        requesLocationDenied();
                    }
                }).ask(REQUEST_CODE_LOCATION); // REQUEST_CODE_LOCATION is what ever int you want (should be distinct)
    }

    private void requesLocationDenied() {
        getUi().showDialog("you must allow to check your location - otherwise I cannot fetch close stations");
    }

    private void requestLocation() {
        if (!PlayServiceChecker.isAvailable(getUi().getContext(), getUi().getFragment().getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST)) {
            getUi().showDialog("Bitte Play Services updaten");
            return;
        }


        if (ActivityCompat.checkSelfPermission(getUi().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(buildGoogleApiClient());

            if (mLastLocation != null) {
                transportService.getStations(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude())).subscribe(
                        stationWrapper -> {
                            getUi().showListOfStations(stationWrapper);
                        }, throwable -> {
                            getUi().showErrorNoStationAvailable();
                        }
                );
            } else {
                getUi().showDialog("Location fetching not succcessfull");
            }
        }
    }

    protected synchronized GoogleApiClient buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getUi().getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        return mGoogleApiClient;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Connection successfull");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
