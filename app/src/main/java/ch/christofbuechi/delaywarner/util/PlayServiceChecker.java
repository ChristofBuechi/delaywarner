package ch.christofbuechi.delaywarner.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by christof on 08.01.17.
 */

public class PlayServiceChecker {

    public static boolean isAvailable(Context context, Activity activity, int returnCode) {
            GoogleApiAvailability gApi = GoogleApiAvailability.getInstance();
            int resultCode = gApi.isGooglePlayServicesAvailable(context);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (gApi.isUserResolvableError(resultCode)) {
                    gApi.getErrorDialog(activity, resultCode, returnCode).show();
                } else {
                    Toast.makeText(context, "not available", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            return true;
        }
}
