package com.stav.hal.action;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 *  Misc permission operations
 */
public class ActionUtils {

  public static boolean isPermissionGranted(Context context, String permission) {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Starts UI dialog asking the user to enable bluetooth
   * @param context context
   */
  public static void requestEnableBluetooth(Context context) {
    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    context.startActivity(enableIntent);
  }

}
