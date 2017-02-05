package ch.christofbuechi.delaywarner;

import android.content.Context;
import android.location.Location;

import java.util.List;

import ch.christofbuechi.delaywarner.base.BaseUI;
import ch.christofbuechi.delaywarner.network.model.Station;

/**
 * Created by Christof on 07.01.2017.
 */
public interface CheckUI extends BaseUI {
    void spinProgress();

    void stopProgress();

    void showErrorcase();

    void updateUI(Location mCurrentLocation, String mLastUpdateTime);

    void showStationList(List<Station> stations);
}
