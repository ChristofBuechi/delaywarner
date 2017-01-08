package ch.christofbuechi.delaywarner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import javax.inject.Inject;

import ch.christofbuechi.delaywarner.base.BaseFragment;
import ch.christofbuechi.delaywarner.base.dagger.AppComponent;
import ch.christofbuechi.delaywarner.network.model.StationWrapper;
import timber.log.Timber;

/**
 * Created by Christof on 07.01.2017.
 */
public class CheckFragment extends BaseFragment<CheckPresenter> implements CheckUI {

    @Inject
    CheckPresenter presenter;
    private boolean animationIsRunning = true;

    public static CheckFragment newInstance() {
        Bundle args = new Bundle();

        CheckFragment fragment = new CheckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.testbutton).setOnClickListener(v -> {
            Timber.wtf("LocateButtonPressed");
            presenter.onButtonPressed();
            CircularProgressView progressView = (CircularProgressView) view.findViewById(R.id.progress_indicator);
            if (animationIsRunning) {
                progressView.stopAnimation();
                animationIsRunning = false;
            } else {
                progressView.startAnimation();
                animationIsRunning = true;
            }
        });

    }

    @Override
    public void injectMembers(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showListOfStations(StationWrapper stationWrapper) {

    }

    @Override
    public void showErrorNoStationAvailable() {

    }

    @Override
    public void showDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, (dialog, which) -> {
                    //no op
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
