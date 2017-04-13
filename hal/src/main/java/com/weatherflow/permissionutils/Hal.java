package com.weatherflow.permissionutils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.weatherflow.permissionutils.listener.PermissionsResultListener;
import com.weatherflow.permissionutils.logging.HalTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import timber.log.Timber;

public class Hal {

  private static final int RC_PERMISSIONS = 13;

  private Activity activity;
  private PermissionsResultListener listener;

  private Set<String> permissions;

  private Hal(Activity activity) {
    permissions = new HashSet<>();
    listener = new PermissionsResultListener.EmptyPermissionsResultListener();
    this.activity = activity;
  }

  /**
   * Initialize HAL
   * @param activity Activity
   * @return Hal instance
   */
  public static Hal with(Activity activity) {
    Timber.d("Good morning, Dave.");
    return new Hal(activity);
  }

  public Hal withListener(PermissionsResultListener listener) {
    this.listener = listener;
    return this;
  }

  /** Add a permission to request. Must be a dangerous permission to be
   * included in the pop up dialog.
   * @param permission One of {@link Manifest.permission}
   * @return Hal instance
   */
  public Hal addPermission(String permission) {
    permissions.add(permission);
    return this;
  }

  /**
   * Request the added permissions
   * @return Hal instance
   */
  public Hal request() {
    ActivityCompat.requestPermissions(activity, getPermissionsAsArray(), RC_PERMISSIONS);
    return this;
  }

  /** Same thing as {@link Hal#request()}, but more thematic given the library's name
   * @return Hal instance
   */
  public Hal openPodBayDoors() {
    return request();
  }

  /** Intercept {@link Activity#onRequestPermissionsResult(int, String[], int[])}
   */
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    this.permissions.clear();

    if(requestCode == RC_PERMISSIONS) {
      List<PermissionResult> permissionResults= getPermissionResults(permissions, grantResults);
      listener.onPermissionsResult(permissionResults);
    }
  }

  /**
   * Must be called in Application onCreate
   * Log debug statements, only works if in debug build.
   */
  public static void enableDebugLog() {
    if(BuildConfig.DEBUG) {
      Timber.plant(new HalTree());
    }
  }

  private String[] getPermissionsAsArray() {
    return permissions.toArray(new String[permissions.size()]);
  }

  private List<PermissionResult> getPermissionResults(String[] permissions, int[] grantResults) {

    List<PermissionResult> permissionResults = new ArrayList<>();

    for(int i = 0; i < permissions.length; i++) {
      boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
      permissionResults.add(new PermissionResult(isGranted, permissions[i]));
    }

    return permissionResults;
  }

}
