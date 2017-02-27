package ch.christofbuechi.delaywarner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import javax.inject.Inject;

import ch.christofbuechi.delaywarner.base.BaseFragment;
import ch.christofbuechi.delaywarner.base.dagger.AppComponent;
import ch.christofbuechi.delaywarner.network.model.Station;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */
public class CheckFragment extends BaseFragment<CheckPresenter> implements CheckUI {

    protected static final String TAG = "location-updates-sample";
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    private static final int REQUEST_PERMISSION_LOCATION = 1;

    @Inject
    CheckPresenter presenter;
    private CircularProgressView progressView;
    private View startSearchButton;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog dialog;

    public static CheckFragment newInstance() {
        Bundle args = new Bundle();

        CheckFragment fragment = new CheckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setUi(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressView = (CircularProgressView) view.findViewById(R.id.progress_indicator);
        startSearchButton = view.findViewById(R.id.checkbutton);
        startSearchButton.setOnClickListener(v -> {
            spinProgress();
            Timber.d("LocateButtonPressed");
            if (isPermissionGranted()) {
                requestLocation();
            } else {
                Timber.d("Berechtigung nicht erteilt");
                showLocationStatePermission();
            }

        });
    }

    private void showLocationStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_LOCATION);
            } else {
                isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_LOCATION);
            }
        } else {
            Toast.makeText(context, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();

        }
    }

    private void isPermissionGranted(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permissionName}, permissionRequestCode);
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog1, id) -> isPermissionGranted(permission, permissionRequestCode));
        builder.create().show();
    }

    private void requestLocation() {
        try {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, presenter.createLocationRequest(), presenter);
            }
        } catch (SecurityException e) {
            Timber.wtf("SecurityException raised");
        }
    }

    private boolean isPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(presenter)
                .addOnConnectionFailedListener(presenter)
                .addApi(LocationServices.API)
                .build();
        presenter.createLocationRequest();
    }

    @Override
    public void spinProgress() {
        startSearchButton.setEnabled(false);
        progressView.startAnimation();
    }

    @Override
    public void stopProgress() {
        startSearchButton.setEnabled(true);
        progressView.stopAnimation();
    }


    @Override
    public void injectMembers(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void showErrorcase() {
        // TODO: 27.01.2017 show dialog
    }

    @Override
    public void updateUI(Location mCurrentLocation, String mLastUpdateTime) {
        String locationText = "Received Location: " + mCurrentLocation.getLatitude() + ": " + mCurrentLocation.getLongitude();
        Toast.makeText(getContext(), locationText, Toast.LENGTH_LONG).show();
        Timber.i(locationText);
    }

    @Override
    public void showStationList(List<Station> stations) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Station station : stations) {
            stringBuilder.append(station.getName() + ":" + station.getDistance() + "m \n");
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(context).setTitle("Stations")
                .setMessage(stringBuilder.toString())
                .setPositiveButton(android.R.string.ok, (dialog1, id) -> dismissDialog())
                .create();

        dialog.show();
        stopProgress();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        buildGoogleApiClient(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, presenter);
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
