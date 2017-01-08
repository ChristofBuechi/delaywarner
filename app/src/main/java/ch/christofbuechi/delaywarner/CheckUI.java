package ch.christofbuechi.delaywarner;

import android.support.v4.app.Fragment;

import ch.christofbuechi.delaywarner.base.BaseUI;
import ch.christofbuechi.delaywarner.network.model.StationWrapper;

/**
 * Created by Christof on 07.01.2017.
 */
public interface CheckUI extends BaseUI {
    Fragment getFragment();

    void showListOfStations(StationWrapper stationWrapper);

    void showErrorNoStationAvailable();

    void showDialog(String s);
}
