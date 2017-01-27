package ch.christofbuechi.delaywarner;

import android.content.Context;
import android.location.Location;

import ch.christofbuechi.delaywarner.base.BaseUI;

/**
 * Created by Christof on 07.01.2017.
 */
public interface CheckUI extends BaseUI {
    void spinProgress();

    void stopProgress();

    void showErrorcase();

    void updateUI(Location mCurrentLocation, String mLastUpdateTime);
}
